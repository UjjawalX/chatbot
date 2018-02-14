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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@CrossOrigin("*")
public class MessagesController {
    private int id = 2000;
    RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger("MessagesController.class");
    String userLocator = "";
    @RequestMapping("/messages")
    public Messages getMessages(){
        RestTemplate restTemplate = new RestTemplate();
        MessageQuote messageQuote = restTemplate.getForObject("http://localhost:3000/messages/1",MessageQuote.class);
        logger.info("The message from JsonServer: "+ messageQuote);
        return new Messages(messageQuote.getId(),messageQuote.getText());
    }
    @RequestMapping(value="/messages", method= RequestMethod.POST )
    public Messages postMessage(@RequestBody MessageQuote messageQuote){
        MessageQuote messages = new MessageQuote();
        NlpProcessor nlpProcessor = new NlpProcessor();
        messages.setText(nlpProcessor.processMsg(messageQuote.getText()));
        messages.setId(Integer.toString(id));
        logger.info("The message from NlpProcessor: "+ messages.getText());
        return new Messages(messages.getId(),messages.getText());
    }
    @RequestMapping(value="/security/loginuser",method= RequestMethod.POST )
    public AuthenticationMessage postAuthenticateUsr(@RequestBody AuthenticationRq authenticationRq){
        AuthenticationDCIdRs authenticationDCIdRs = null;
        AuthenticationDCIdRq authenticationDCIdRq = new AuthenticationDCIdRq();
        if(authenticationRq!=null&&!authenticationRq.getUserid().equals("")){
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
            authenticationDCIdRs = restTemplate.postForObject("http://blrcslfbpub0006.misys.global.ad:9080/foundation-guiwar/security/pegasus_security_check",authenticationDCIdRq, AuthenticationDCIdRs.class);
        }
        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
        if(authenticationDCIdRs != null && authenticationDCIdRs.isSuccess()){
            authenticationMessage.setStatus("success");
            authenticationMessage.setMessage("DC user authorized");
            logger.info("DC user authorized");
        }
        else{
            authenticationMessage.setMessage("authorization failure");
            authenticationMessage.setStatus("failure");
        }

        return authenticationMessage;
    }

    @RequestMapping(value="/security/loginpass",method = RequestMethod.POST )
    public AuthenticationMessage postAuthenticatePass(@RequestBody AuthenticationRq authenticationRq){
        AuthenticationDCPassRq authenticationDCPassRq = new AuthenticationDCPassRq();
        AuthenticationMessage authenticationMessage = new AuthenticationMessage();
        AuthenticationDCPassRs authenticationDCPassRs = null;
        if(authenticationRq!=null&&!authenticationRq.getPassword().equals("")){
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
            authenticationDCPassRs = restTemplate.postForObject("http://blrcslfbpub0006.misys.global.ad:9080/foundation-guiwar/security/pegasus_security_check",authenticationDCPassRq,AuthenticationDCPassRs.class);
        }
        if(authenticationDCPassRs.isSuccess()){
            logger.info("DC password authentication succeded");
            authenticationMessage.setStatus("success");
            authenticationMessage.setMessage("DC password authentication succeded");
        }
        else {
            logger.info("failed : DC password authentication failed");
            authenticationMessage.setStatus("fail");
            authenticationMessage.setMessage("failed : DC password authentication failed");
        }
        return authenticationMessage;
    }
}
