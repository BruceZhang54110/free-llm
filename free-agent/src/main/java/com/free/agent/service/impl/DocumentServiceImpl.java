package com.free.agent.service.impl;

import com.free.agent.common.MinioUploadType;
import com.free.agent.config.MinioConfiguration;
import com.free.agent.db.service.UploadFilesService;
import com.free.agent.service.DocumentService;
import com.free.agent.service.MinioService;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final UploadFilesService uploadFilesService;

    private final MinioService minioService;

    /**
     * 上传文件到minio
     *
     * @param uploadFile uploadFile
     * @return 上传成功
     */
    @Override
    public boolean uploadFileToMinio(MultipartFile uploadFile) {
        minioService.uploadMultipartFile("free-agent", uploadFile, MinioUploadType.REGISTER_FILE);
        return true;
    }
}
