package com.free.agent.config;

import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfiguration {
    @Bean
    DocumentParser apacheTikaDocumentParser() {
        return new ApacheTikaDocumentParser();
    }
}
