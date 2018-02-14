package com.misys.hackathon.chatbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageQuote {
    private String id;
    private String text;

    public MessageQuote() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MessageQuote{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
