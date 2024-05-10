package com.free.ollama.config;

import com.free.ollama.aiservice.Assistant;
import com.free.ollama.aiservice.DocsEmbeddingsService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Configuration
public class AssistantConfiguration {

    /**
     * This chat memory will be used by an {@link Assistant}
     */
    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }


    /**
     * embeddingModel
     * @return
     */
    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Qualifier("pgVectorEmbeddingStore")
    @Autowired
    private EmbeddingStore pgVectorEmbeddingStore;

    /**
     * embeddingStore
     * @param embeddingModel
     * @param resourceLoader
     * @return
     * @throws IOException
     */
    //@Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) throws IOException {
        // 1. Create an in-memory embedding store
        EmbeddingStore<TextSegment> embeddingStore = pgVectorEmbeddingStore;
        // 2. Load an example document ("Miles of Smiles" terms of use)
        Resource resource = resourceLoader.getResource("classpath:example-files/miles-of-smiles-terms-of-use.txt");
        Document document = loadDocument(resource.getFile().toPath(), new TextDocumentParser());
        // 3. Split the document into segments 100 tokens each
        // 4. Convert segments into embeddings
        // 5. Store embeddings into embedding store
        // All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0);
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);
        return embeddingStore;
    }


    /**
     * 内容召回,检索
     * retriever 中文翻译有猎犬的意思，就是根据输入的query，找到最相似的文档
     * @param embeddingStore
     * @param embeddingModel
     * @return
     */
    // @Bean
    ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel pgVectorEmbeddingStore) {
        // You will need to adjust these parameters to find the optimal setting, which will depend on two main factors:
        // - The nature of your data
        // - The embedding model you are using
        int maxResults = 3;
        double minScore = 0.0;
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(pgVectorEmbeddingStore)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();

    }

    /**
     *
     * @param chatLanguageModel
     * @return
     */
    @Bean
    public QueryTransformer queryTransformer(ChatLanguageModel chatLanguageModel) {
        return new CompressingQueryTransformer(chatLanguageModel);
    }

    @Bean
    RetrievalAugmentor retrievalAugmentor(QueryTransformer queryTransformer, EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        int maxResults = 3;
        double minScore = 0.0;
        ContentRetriever contentRetriever =  EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
        return DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();
    }





    @Bean
    CommandLineRunner docsToEmbeddings(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, ResourceLoader resourceLoader) {
        return args -> {
            Resource resource = resourceLoader.getResource("classpath:example-files/miles-of-smiles-terms-of-use.txt");
            Document document = loadDocument(resource.getFile().toPath());
            DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0);
            var ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(documentSplitter)
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();
            ingestor.ingest(document);
        };
    }

}
