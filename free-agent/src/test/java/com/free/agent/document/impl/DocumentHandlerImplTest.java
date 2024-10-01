package com.free.agent.document.impl;

import com.free.agent.document.DocumentHandler;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class DocumentHandlerImplTest {

    @Autowired
    private DocumentHandler documentHandler;

    @Test
    void parse() {
    }

    @Test
    void splitDocument() {
        Document parse = documentHandler.parseLocalFile("/Users/miles-of-smiles-terms-of-use.txt");
        System.out.println(parse.text().length());
        List<TextSegment> textSegments = documentHandler.splitDocument(Collections.singletonList(parse), 300, 10);
        textSegments.forEach(System.out::println);

    }
}