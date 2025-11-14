package com.favour.web_lab4_backend.dtos;

// DTO for incoming point check request from the frontend
public class ResultRequest {
    private Double x;
    private Double y;
    private Double r;

    // Getters
    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }

    // Setters
    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setR(Double r) {
        this.r = r;
    }
}