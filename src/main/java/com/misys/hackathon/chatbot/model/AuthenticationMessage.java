package com.misys.hackathon.chatbot.model;

public class AuthenticationMessage {
    String status;
    String message;

    public AuthenticationMessage() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
