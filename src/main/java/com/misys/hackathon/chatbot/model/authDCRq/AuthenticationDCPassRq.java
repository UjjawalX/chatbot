package com.misys.hackathon.chatbot.model.authDCRq;

public class AuthenticationDCPassRq {
    Header1 header;
    Data1 data;

    public AuthenticationDCPassRq() {
        header = null;
        data = null;
    }

    public Header1 getHeader() {
        return header;
    }

    public void setHeader(Header1 header) {
        this.header = header;
    }

    public Data1 getData() {
        return data;
    }

    public void setData(Data1 data) {
        this.data = data;
    }
}
