package com.debtgo.debtgo_backend.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private Long id;
    private String email;
    private String role;

    public LoginResponse(boolean success, String message, Long id, String email, String role) {
        this.success = success;
        this.message = message;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}