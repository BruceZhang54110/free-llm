package com.free.agent.document.impl;

import com.free.agent.document.DocumentHandler;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DocumentHandlerImpl implements DocumentHandler {

    private final DocumentParser apacheTikaDocumentParser;

    @Override
    public Document parseLocalFile(String fileName) {
        InputStream fileInputStream = null;
        try {
            File file = FileUtils.getFile(fileName);
            fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assert.notNull(fileInputStream, "fileName:" + fileName + ", inputStream is null");
        return apacheTikaDocumentParser.parse(fileInputStream);
    }

    /**
     * 文档拆分
     *
     * @param documents             文档
     * @param maxSegmentSizeInChars 最大字符数
     * @param maxOverlapSizeInChars 最大重叠字符数
     * @return
     */
    @Override
    public List<TextSegment> splitDocument(List<Document> documents, int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        DocumentSplitter splitter = DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars);
        return splitter.splitAll(documents);
    }

}
