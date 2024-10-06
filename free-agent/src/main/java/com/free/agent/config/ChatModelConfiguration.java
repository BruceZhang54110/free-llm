package com.free.agent.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "zhipu-ai-chat-model")
@Configuration
public class ChatModelConfiguration {

    private String model;

    private String apiKey;

    private Integer maxRetries;

    private Integer maxToken;

    private Double topP;

    private Double temperature;

    @Bean
    public ChatLanguageModel zhipuAiChatModel() {
        return ZhipuAiChatModel.builder()
                .model(model)
                .apiKey(apiKey)
                .maxRetries(maxRetries)
                .maxToken(maxToken)
                .topP(topP)
                .temperature(temperature)
                .build();
    }
}
