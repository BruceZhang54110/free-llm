package com.free.agent.service;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface DocumentService {

    /**
     * 上传文件到minio
     * @param uploadFile uploadFile
     * @return 上传成功
     */
    boolean uploadFileToMinio(MultipartFile uploadFile);

}
