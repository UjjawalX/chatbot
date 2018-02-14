package com.misys.hackathon.chatbot.model;

public class Messages {
    private String id;
    private String messages;

    public Messages() {
    }

    public Messages(String id, String messages) {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
