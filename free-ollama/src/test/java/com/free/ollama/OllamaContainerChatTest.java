package com.free.ollama;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 拉去llama2镜像
 */
@Testcontainers
public class OllamaContainerChatTest {

    /**
     * 模型名称
     * ollama支持的模型可以查看 https://ollama.com/library
     */
    static String MODEL_NAME = "llama2"; // try "mistral", "llama2", "codellama", "phi" or "tinyllama"

    @Container
    static GenericContainer<?> ollama = new GenericContainer<>("langchain4j/ollama-" + MODEL_NAME + ":latest")
            .withExposedPorts(11434);

    static String baseUrl() {
        return String.format("http://%s:%d", ollama.getHost(), ollama.getFirstMappedPort());
    }

    @Test
    void simpleChatExample() {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl(baseUrl())
                .modelName(MODEL_NAME)
                .format("json")
                .build();

        String json = model.generate("Give me a JSON with 2 fields: name and age of a John Doe, 42");

        System.out.println(json);

    }


}
