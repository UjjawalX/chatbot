package com.misys.hackathon.chatbot.model.authDCRq;

public class Header1 {
    String token;
    Integer conversationScopeId;

    public Header1() {
        token = null;
        conversationScopeId = 1;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getConversationScopeId() {
        return conversationScopeId;
    }

    public void setConversationScopeId(Integer conversationScopeId) {
        this.conversationScopeId = conversationScopeId;
    }
}
