package com.free.agent.controller.doc;

import com.free.agent.common.FResponse;
import com.free.agent.common.FResult;
import com.free.agent.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/doc")
@RestController
public class DocumentController {

    private final DocumentService documentService;


    /**
     * upload file api
     * @param uploadFile uploadFile
     */
    @PostMapping("/file")
    public FResponse<Boolean> uploadFile(@RequestParam("file") MultipartFile uploadFile) {
        return FResult.success(documentService.uploadFileToMinio(uploadFile));
    }
}
