package com.example.futasbus.request;



public class OtpRequest {
    private String email;

    public OtpRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}