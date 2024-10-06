package com.free.agent.config;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class EmbeddingModelConfiguration {

    /*public EmbeddingModel embeddingModel() {
        return ZhipuAiEmbeddingModel.builder()
                .apiKey("apiKey")
                .model(dev.langchain4j.model.zhipu.embedding.EmbeddingModel.EMBEDDING_2.toString())
                .logRequests(true)
                .logResponses(true)
                .maxRetries(1)
                .build();
    }*/
}
