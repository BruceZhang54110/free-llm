package com.free.ollama.aiservice;

import org.springframework.core.io.ResourceLoader;

@FunctionalInterface
public interface DocsEmbeddingsService {

    void docsToEmbeddings(ResourceLoader resourceLoader);
}
