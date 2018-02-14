package com.misys.hackathon.chatbot.model;



public class AuthenticationRq {
    private String userid;
    private String password;

    public AuthenticationRq() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
