package com.free.ollama.controller;

import com.free.ollama.aiservice.Assistant;
import dev.langchain4j.service.TokenStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {

    private Assistant assistant;

    public ChatController(Assistant assistant) {
        this.assistant = assistant;
    }

    @Qualifier("taskExecutor1")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor1;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/query")
    public String assistant(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }

    @RequestMapping("/stream")
    public SseEmitter assistantStream(@RequestParam(value = "message") String message
    , HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        SseEmitter emitter = new SseEmitter();

        TokenStream tokenStream = assistant.chatStream(message);
        tokenStream.onNext(s -> {
            CompletableFuture.runAsync(() -> {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("send message:" + message);
                    }
                    emitter.send(message, MediaType.TEXT_EVENT_STREAM);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }, taskExecutor1);
        }).onComplete(aiMessageResponse -> {emitter.complete();}).onError(throwable -> log.error(throwable.getMessage(), throwable)).start();

        return emitter;
    }

    private ExecutorService nonBlockingService = Executors
            .newCachedThreadPool();
    @GetMapping("/srb")
    public ResponseEntity<SseEmitter> handleRbe(@RequestParam(value = "inputParameter") String inputParameter, HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        SseEmitter emitter = new SseEmitter();
        new Thread(() -> {
            try {
                // Query the database based on the input parameter and send data in batches
                for (int i = 0; i < 10; i++) {
                    String data = "Data batch " + i + " for parameter: " + inputParameter;
                    emitter.send(data);
                    Thread.sleep(1000); // Simulate delay between batches
                }

                emitter.complete(); // Complete the SSE connection
            } catch (Exception e) {
                emitter.completeWithError(e); // Handle errors
            }
        }).start();
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }
}
