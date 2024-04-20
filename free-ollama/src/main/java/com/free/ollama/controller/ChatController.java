package com.free.ollama.controller;

import com.free.ollama.aiservice.Assistant;
import dev.langchain4j.service.TokenStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Controller
public class ChatController {

    private Assistant assistant;

    public ChatController(Assistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/assistant")
    public String assistant(@RequestParam(value = "message", defaultValue = "What is the Chinese time now?") String message) {
        return assistant.chat(message);
    }

    @RequestMapping("/assistant1")
    public SseEmitter assistantStream(@RequestParam(value = "message", defaultValue = "What is the Chinese time now?") String message
    , HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        SseEmitter emitter = new SseEmitter();
        TokenStream tokenStream = assistant.chatStream(message);
        tokenStream.onNext(s -> {
            try {
                System.out.println("=" + s);
                emitter.send(s, MediaType.TEXT_EVENT_STREAM);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                emitter.completeWithError(e);
            }
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
