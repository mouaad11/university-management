package com.etablissement.fullstack_backend.payload.request;

public class LoginRequest {
    private String username;
    private String password;
    // Getters and setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
