package com.free.ollama;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;


@Slf4j
@SpringBootTest
public class ChatMessagesTest {

    ChatLanguageModel ollamaChatModel;

    @BeforeEach
    void init() {
        ollamaChatModel = OllamaChatModel.builder()
                .modelName("llama2")
                .baseUrl("http://localhost:11434")
                //.format("json")
                .build();
    }

    @Test
    public void ollamaChatModelTest() {
        long startMillis = System.currentTimeMillis();
        String generate = ollamaChatModel.generate("为什么天空是蓝色的？");
        long endMills = System.currentTimeMillis();
        System.out.println(generate);
        System.out.println("耗时：" + (endMills - startMillis) + " ms");
    }

    @Test
    public void ollamaChatModelTest2() {
        StreamingChatLanguageModel model = OllamaStreamingChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma")
                .temperature(0.0)
                .numCtx(2048)
                .build();
        CompletableFuture<Response<AiMessage>> futureResponse = new CompletableFuture<>();
        String userMessage = "为什么天空是蓝色的？";
        model.generate(userMessage, new StreamingResponseHandler<>() {
            @Override
            public void onNext(String token) {
                System.out.print(token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                futureResponse.complete(response);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }

        });
        futureResponse.join();
    }


    /**
     * provide multiple ChatMessages as input, instead of just one?
     * This is because LLMs are stateless by nature,
     * meaning they do not maintain the state of the conversation.
     * So, if you want to support multi-turn conversations,
     * you should take care of managing the state of the conversation.
     */
    @Test
    public void testMultipleChatMessages() {
        UserMessage firstUserMessage = UserMessage.from("Hello, my name is Klaus");
        log.info("user say:{}", firstUserMessage.contents().get(0));
        AiMessage firstAiMessage = ollamaChatModel.generate(firstUserMessage).content();
        log.info("ai say:{}", firstAiMessage.text());
        UserMessage secondUserMessage = UserMessage.from("What is my name?");
        log.info("user say:{}", secondUserMessage.contents().get(0));
        AiMessage content = ollamaChatModel.generate(firstUserMessage, firstAiMessage, secondUserMessage).content();
        log.info("ai say:{}", content.text());

    }

}
