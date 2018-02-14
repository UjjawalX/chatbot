package com.misys.hackathon.chatbot.model.authDCRq;

public class Header {
    String token;
    Integer conversationScopeId;
    CaptchaData userInputCaptchaData;

    public Header() {
        token = null;
        conversationScopeId = 1;
        userInputCaptchaData = null;
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

    public CaptchaData getUserInputCaptchaData() {
        return userInputCaptchaData;
    }

    public void setUserInputCaptchaData(CaptchaData userInputCaptchaData) {
        this.userInputCaptchaData = userInputCaptchaData;
    }
}
