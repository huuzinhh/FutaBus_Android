package com.example.futasbus.model;

public class ChatMessage {
    private String message;
    private boolean isUser;

    public ChatMessage() {}

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public ChatMessage(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUser() { return isUser; }

    public void setUser(boolean user) {
        isUser = user;
    }
}

