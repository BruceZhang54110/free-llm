package com.free.agent.embddding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ZhipuAiEmbeddingTests {

    @Autowired
    private EmbeddingModel zhipuAiEmbeddingModel;

    @Test
    public void zhipuEmbeding() {
        Response<Embedding> embed = zhipuAiEmbeddingModel.embed("hello world");
        System.out.println(embed.toString());
        assertThat(embed.content().dimension()).isEqualTo(1024);

    }
}
