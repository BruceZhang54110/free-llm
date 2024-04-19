package com.free.ollama;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Slf4j
public class RagTestCase {

    public static void main(String[] args) {
        loadSingleDocument();
    }
    private static void loadSingleDocument() {
        Path documentPath = toPath("example-files/story-about-happy-carrot.pdf");
        log.info("Loading single document: {}", documentPath);
        Document document = loadDocument(documentPath, new ApacheTikaDocumentParser());
        System.out.println(document.text());
    }

    private static Path toPath(String fileName) {
        try {
            URL fileUrl = RagTestCase.class.getClassLoader().getResource(fileName);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(Document document) {
        log.info("{}: {} ...", document.metadata("file_name"), document.text().trim().substring(0, 50));
    }
}
