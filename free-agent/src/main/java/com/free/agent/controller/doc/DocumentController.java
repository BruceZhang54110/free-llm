package com.free.agent.controller.doc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/doc")
@RestController
public class DocumentController {


    /**
     * 上传文件
     * @param uploadFile uploadFile
     */
    @PostMapping("/file")
    public void uploadFile(@RequestParam("file") MultipartFile uploadFile) {

    }
}
