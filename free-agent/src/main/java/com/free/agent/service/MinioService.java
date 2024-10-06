package com.free.agent.service;

import com.free.agent.common.MinioUploadPath;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadMultipartFile(MultipartFile multipartFile, MinioUploadPath minioUploadPath);
}
