package com.free.agent.document;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DocumentHandler {


    /**
     * 文档文件解析
     * @param fileName 文件名
     * @return Document
     */
    Document parseLocalFile(String fileName);

    /**
     * 文档拆分
     * @param documents 文档
     * @param maxSegmentSizeInChars 最大字符数
     * @param maxOverlapSizeInChars 最大重叠字符数
     * @return
     */
    List<TextSegment> splitDocument(List<Document> documents, int maxSegmentSizeInChars, int maxOverlapSizeInChars);
}
