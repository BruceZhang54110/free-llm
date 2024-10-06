package com.free.agent.service;

import com.free.agent.common.MinioUploadPath;
import io.minio.ObjectWriteResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    ObjectWriteResponse uploadMultipartFile(MultipartFile multipartFile, MinioUploadPath minioUploadPath);
}
