package com.free.agent.service;

import com.free.agent.common.MinioUploadType;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadMultipartFile(String bucketName, MultipartFile multipartFile, MinioUploadType minioUploadType);
}
