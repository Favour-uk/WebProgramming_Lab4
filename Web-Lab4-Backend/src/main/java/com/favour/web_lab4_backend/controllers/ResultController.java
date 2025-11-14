package com.favour.web_lab4_backend.controllers;

import com.favour.web_lab4_backend.dtos.ResultRequest;
import com.favour.web_lab4_backend.models.Result;
import com.favour.web_lab4_backend.models.User;
import com.favour.web_lab4_backend.repositories.ResultRepository;
import com.favour.web_lab4_backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultRepository resultRepository;
    private final UserRepository userRepository;

    public ResultController(ResultRepository resultRepository, UserRepository userRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    // Helper method to determine the user from the current session
    private Optional<User> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    /**
     * POST endpoint to check a point and save the result.
     * Requires authentication handled by SecurityConfig.
     */
    @PostMapping
    public ResponseEntity<?> checkPoint(@RequestBody ResultRequest request, Authentication authentication) {

        Optional<User> userOptional = getCurrentUser(authentication);
        if (userOptional.isEmpty()) {
            // Should be caught by SecurityConfig 401/403, but this is a safeguard
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User session not found.");
        }
        User user = userOptional.get();

        // Data validation (R must be positive for the geometry to work)
        if (request.getX() == null || request.getY() == null || request.getR() == null || request.getR() <= 0) {
            return ResponseEntity.badRequest().body("Invalid input for X, Y, or R."); // 400 Bad Request
        }

        // Calculation Logic
        boolean isHit = checkArea(request.getX(), request.getY(), request.getR());

        // Create and save result
        Result result = new Result();
        result.setX(request.getX());
        result.setY(request.getY());
        result.setR(request.getR());
        result.setHit(isHit);
        result.setUser(user);

        Result savedResult = resultRepository.save(result);

        // Return the saved object, including the generated ID and hit status
        return ResponseEntity.ok(savedResult);
    }

    /**
     * GET endpoint to retrieve the user's history.
     * Requires authentication.
     */
    @GetMapping
    public ResponseEntity<?> getHistory(Authentication authentication) {
        Optional<User> userOptional = getCurrentUser(authentication);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User session not found.");
        }

        // Fetch history for the current authenticated user
        List<Result> history = resultRepository.findByUserId(userOptional.get().getId());

        return ResponseEntity.ok(history);
    }

    // --- Area Check Logic (FINAL CORRECTION) ---
    private boolean checkArea(double x, double y, double r) {
        final double r_abs = Math.abs(r); // Use absolute R for all geometry checks
        final double r_half = r_abs / 2.0; // Radius R/2 for circle and rectangle dimensions

        // I Quadrant (x >= 0, y >= 0): Area is a TRIANGLE (Vertices: (0,0), (R,0), (0,R))
        // Check: y <= R - x
        if (x >= 0 && y >= 0) {
            return (y <= r_abs - x);
        }

        // II Quadrant (x < 0, y >= 0): No area covered
        else if (x < 0 && y >= 0) {
            return false;
        }

        // III Quadrant (x < 0, y < 0): Area is a RECTANGLE (x from -R/2 to 0, y from -R to 0)
        // Check: -R/2 <= x <= 0 AND -R <= y <= 0
        else if (x < 0 && y < 0) {
            return (x >= -r_half && x <= 0 && y >= -r_abs && y <= 0);
        }

        // IV Quadrant (x >= 0, y < 0): Area is a QUARTER CIRCLE (Radius R/2)
        // 🔥 FIX: Check against (R/2)^2
        else if (x >= 0 && y < 0) {
            return (x * x + y * y <= r_half * r_half);
        }

        return false;
    }
}