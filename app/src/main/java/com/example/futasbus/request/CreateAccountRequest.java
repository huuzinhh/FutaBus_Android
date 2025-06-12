package com.example.futasbus.request;

public class CreateAccountRequest {
    private String email;
    private String password;

    public CreateAccountRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters & Setters nếu cần
}

