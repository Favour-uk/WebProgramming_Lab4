package com.favour.web_lab4_backend.dtos;

public class RegisterRequest {
    private String username;
    private String password;

    // Default constructor (required by some JSON/Spring serialization)
    public RegisterRequest() {
    }

    // Constructor for convenience
    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}