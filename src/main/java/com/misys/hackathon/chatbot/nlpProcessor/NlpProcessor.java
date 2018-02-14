package com.misys.hackathon.chatbot.nlpProcessor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class NlpProcessor {
    String responseMsg;
    public String processMsg(String msg){
        responseMsg =  "I didn't understand the message";
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(msg);

        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                System.out.print("Token: "+word);
                if(word.equalsIgnoreCase("Hi")||word.equalsIgnoreCase("Hello")||word.equalsIgnoreCase("Hola"))
                    return "Hi welcome to FusionBot . How can I help you";
                else if(word.equalsIgnoreCase("accountStatement")||word.equalsIgnoreCase("miniStatement")){
                    return "Partner Name: FoooShop ||  Amount: Rs 1000 || Date: 21/12/2018";
                }
                else if(word.equalsIgnoreCase("transfer")){
                    return "Enter the beneficiary name";
                }
                else if(word.equalsIgnoreCase("John")||word.equalsIgnoreCase("Rahul")||word.equalsIgnoreCase("Divya")){
                    return "Enter the amount";
                }
                else if(word.equalsIgnoreCase("Rs")) {
                    return "The amount has been transferred";
                }
                else
                    continue;
            }
        }
        return responseMsg;
    }
}
