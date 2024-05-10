package com.free.ollama.config;

import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EmbeddingStoreConfiguration {

    @Value("${pg.vector.host}")
    private String pgVectorHost;

    @Value("${pg.vector.port}")
    private int pgVectorPort;

    @Value("${pg.vector.database}")
    private String pgVectorDatabase;

    @Value("${pg.vector.user}")
    private String pgVectorUser;

    @Value("${pg.vector.password}")
    private String pgVectorPassword;

    @Value("${pg.vector.table}")
    private String pgVectorTable;

    @Value("${pg.vector.dimension}")
    private Integer pgVectorDimension;

    @Value("${pg.vector.indexListSize}")
    private Integer pgVectorIndexListSize;

    /**
     * pgVectorEmbeddingStore bean
     * @return
     */
    @ConditionalOnProperty(value = "pg.vector.enable", havingValue = "true")
    @Bean
    public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
        log.info("PgVectorEmbeddingStore build start...");
        PgVectorEmbeddingStore pgVectorEmbeddingStore = PgVectorEmbeddingStore.builder()
                .host(pgVectorHost)
                .port(pgVectorPort)
                .database(pgVectorDatabase)
                .useIndex(true)
                .user(pgVectorUser)
                .password(pgVectorPassword)
                .dimension(pgVectorDimension)
                .indexListSize(pgVectorIndexListSize)
                .createTable(true)
                .table(pgVectorTable)
                .dropTableFirst(true)
                .build();
        log.info("PgVectorEmbeddingStore build success");
        return pgVectorEmbeddingStore;
    }
}
