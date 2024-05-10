package com.free.ollama.aiservice;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage("You are a polite assistant, and your name is Jack, you must answer question in 简体中文")
    String chat(String message);

    @SystemMessage("You are a polite assistant, and your name is Jack,  you must answer question in 简体中文")
    TokenStream chatStream(String message);

}
