package com.misys.hackathon.chatbot.model.authDCRq;

public class AuthenticationDCIdRq {
    Header header;
    Data data;

    public AuthenticationDCIdRq() {
        header = null;
        data = null;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
