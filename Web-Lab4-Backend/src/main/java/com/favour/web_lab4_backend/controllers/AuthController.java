package com.favour.web_lab4_backend.controllers;

import com.favour.web_lab4_backend.dtos.LoginRequest;
import com.favour.web_lab4_backend.dtos.RegisterRequest;
import com.favour.web_lab4_backend.models.User;
import com.favour.web_lab4_backend.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // NOTE: your User entity currently doesn't persist roles; UserService grants ROLE_USER at runtime
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        try {
            // 1) Authenticate credentials using the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2) Create and set the SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // 3) Ensure HTTP session exists and store SecurityContext in session (so JSESSIONID is sent)
            HttpSession session = request.getSession(true); // create session if not present
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            // 4) Log session id for debugging (server logs)
            System.out.println("User '" + loginRequest.getUsername() + "' authenticated. Session id: " + session.getId());

            return ResponseEntity.ok("User logged in successfully");
        } catch (Exception e) {
            System.err.println("Authentication failed for user: " + loginRequest.getUsername() + " Error: " + e.getMessage());
            return new ResponseEntity<>("Invalid credentials or server error.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Logged out");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed.");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))) {
            return ResponseEntity.ok("Session is active for user: " + auth.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session.");
    }
}
