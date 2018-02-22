package com.misys.hackathon.chatbot.controller;

import com.misys.hackathon.chatbot.model.AuthenticationMessage;
import com.misys.hackathon.chatbot.model.AuthenticationRq;
import com.misys.hackathon.chatbot.model.MessageQuote;
import com.misys.hackathon.chatbot.model.Messages;
import com.misys.hackathon.chatbot.model.authDCRq.*;
import com.misys.hackathon.chatbot.model.authDCRs.AuthenticationDCIdRs;
import com.misys.hackathon.chatbot.model.authDCRs.AuthenticationDCPassRs;
import com.misys.hackathon.chatbot.nlpProcessor.NlpProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin("*")
public class MessagesController {
    private int id = 2000;
    RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger("MessagesController.class");
    String userLocator = "";
    String sessionId;
    String dcUrl = "http://fr1csldcdev0013.misys.global.ad:9086/foundation-guiwar/security/pegasus_security_check";

    @RequestMapping("/messages")
    public Messages getMessages() {
        RestTemplate restTemplate = new RestTemplate();
        MessageQuote messageQuote = restTemplate.getForObject("http://localhost:3000/messages/1", MessageQuote.class);
        logger.info("The message from JsonServer: " + messageQuote);
        return new Messages(messageQuote.getId(), messageQuote.getText());
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public Messages postMessage(@RequestBody MessageQuote messageQuote) {
        MessageQuote messages = new MessageQuote();
        NlpProcessor nlpProcessor = new NlpProcessor();
        messages.setText(nlpProcessor.processMsg(messageQuote.getText()));
        messages.setId(Integer.toString(id));
        logger.info("The message from NlpProcessor: " + messages.getText());
        return new Messages(messages.getId(), messages.getText());
    }

    @RequestMapping(value = "/security/loginuser", method = RequestMethod.POST)
    public AuthenticationMessage postAuthenticateUsr(@RequestBody AuthenticationRq authenticationRq) {
        HttpEntity<AuthenticationDCIdRs> authenticationDCIdResponse;
        HttpEntity<AuthenticationDCPassRs> authenticationDCPassResponse;
        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
        if (authenticationRq != null && !authenticationRq.getUserid().equals("") && !authenticationRq.getPassword().equals("")) {
            authenticationDCIdResponse = authenticateUser(authenticationRq);
            if (authenticationDCIdResponse != null && authenticationDCIdResponse.getBody() != null && authenticationDCIdResponse.getBody().isSuccess()) {
                logger.info("DC user name authorized");
                authenticationDCPassResponse = authenticatePass(authenticationRq);
                if (authenticationDCPassResponse != null && authenticationDCPassResponse.getBody() != null && authenticationDCPassResponse.getBody().isSuccess()) {
                    logger.info("DC password authentication succeded");
                    userLocator = authenticationDCPassResponse.getBody().getHeader().getToken();
                    logger.info("Userlocator : "+userLocator);
                    authenticationMessage.setStatus("success");
                    authenticationMessage.setMessage("DC authentication succeded");
                } else {
                    logger.info("failed : DC password authentication failed");
                    authenticationMessage.setStatus("fail");
                    authenticationMessage.setMessage("failed : DC authentication failed");
                }
            } else {
                logger.info("user name authorization failure");
                authenticationMessage.setMessage("user name authorization failure");
                authenticationMessage.setStatus("failure");
            }
        }
        return authenticationMessage;
    }

    private HttpEntity<AuthenticationDCIdRs> authenticateUser(AuthenticationRq authenticationRq) {
        AuthenticationDCIdRq authenticationDCIdRq = new AuthenticationDCIdRq();
        HttpEntity<AuthenticationDCIdRs> response;
        //set Data
        UserData userData = new UserData();
        userData.setLoginID(authenticationRq.getUserid());
        Data data = new Data();
        data.setData(userData);
        authenticationDCIdRq.setData(data);
        //set header
        Header header = new Header();
        CaptchaData captchaData = new CaptchaData();
        header.setConversationScopeId(1);
        header.setUserInputCaptchaData(captchaData);
        authenticationDCIdRq.setHeader(header);
        //service call
        HttpEntity<AuthenticationDCIdRq> request = new HttpEntity<>(authenticationDCIdRq);
        response = restTemplate.exchange(dcUrl, HttpMethod.POST, request, AuthenticationDCIdRs.class);
        List<String> cookie = response.getHeaders().get("Set-Cookie");
        sessionId = Arrays.asList(cookie.get(0).split(";")).get(0);
        logger.info("Set-Cookie: " + cookie.toString());
        logger.info("Session Id: " + sessionId);
        return response;
    }

    public HttpEntity<AuthenticationDCPassRs> authenticatePass(AuthenticationRq authenticationRq) {
        AuthenticationDCPassRq authenticationDCPassRq = new AuthenticationDCPassRq();
        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
        HttpEntity<AuthenticationDCPassRq> request = null;
        HttpEntity<AuthenticationDCPassRs> response = null;

        //set header
        Header1 header1 = new Header1();
        authenticationDCPassRq.setHeader(header1);
        //set data
        UserData1 userData1 = new UserData1();
        userData1.setCredentials(authenticationRq.getPassword());
        Data1 data = new Data1();
        data.setData(userData1);
        authenticationDCPassRq.setData(data);
        //service call
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("cookie", sessionId);
        request = new HttpEntity(authenticationDCPassRq, httpHeaders);
        response = restTemplate.exchange(dcUrl, HttpMethod.POST, request, AuthenticationDCPassRs.class);
        return response;
    }
}
