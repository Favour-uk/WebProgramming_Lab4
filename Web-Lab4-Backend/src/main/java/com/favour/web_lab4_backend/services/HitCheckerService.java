package com.favour.web_lab4_backend.services;

import org.springframework.stereotype.Service;

@Service
public class HitCheckerService {

    public boolean checkHit(double x, double y, double r) {
        if (r <= 0) {
            return false;
        }

        // --- Geometric Area Check (Based on the image) ---

        // Quadrant I: Triangle (x >= 0, y >= 0, y <= R - x)
        if (x >= 0 && y >= 0) {
            return y <= r - x;
        }

        // Quadrant II: Empty
        if (x < 0 && y > 0) {
            return false;
        }

        // Quadrant III: Rectangle (x <= 0, y <= 0, x >= -R, y >= -R)
        if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r;
        }

        // Quadrant IV: Quarter Circle (x > 0, y < 0, x^2 + y^2 <= (R/2)^2)
        if (x > 0 && y < 0) {
            double radiusSquared = (r / 2.0) * (r / 2.0);
            return (x * x + y * y) <= radiusSquared;
        }

        return false;
    }
}