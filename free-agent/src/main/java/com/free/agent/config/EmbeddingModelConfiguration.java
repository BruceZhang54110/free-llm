package com.free.agent.config;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "zhipu-ai-embedding-model")
@Data
@Configuration
public class EmbeddingModelConfiguration {

    private String apiKey;

    private Integer maxRetries;

    private String model;

    private Boolean logRequests;

    private Boolean logResponses;

    @Bean
    public EmbeddingModel zhipuAiEmbeddingModel() {
        return ZhipuAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .model(model)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .maxRetries(maxRetries)
                .build();
    }
}
