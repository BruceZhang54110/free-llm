package com.free.ollama.aiservice;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage("You are a polite assistant with Chinese")
    String chat(String message);

}
