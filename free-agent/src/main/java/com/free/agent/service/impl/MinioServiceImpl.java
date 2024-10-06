package com.free.agent.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.free.agent.common.MinioUploadPath;
import com.free.agent.common.ResultCode;
import com.free.agent.common.ex.BusinessException;
import com.free.agent.config.MinioConfiguration;
import com.free.agent.service.MinioService;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    public String uploadMultipartFile(MultipartFile multipartFile, MinioUploadPath minioUploadPath) {
        try {
            String extName = FileUtil.extName(multipartFile.getOriginalFilename());
            String objectName = minioUploadPath.getObjectPath() + DateUtil.today() + File.separator + IdUtil.simpleUUID() + "." + extName;
            PutObjectArgs putObjectArgs = MinioConfiguration.buildPutObjectArgs(MinioUploadPath.BUCKET_NAME.getObjectPath())
                    .object(objectName)
                    .contentType(multipartFile.getContentType())
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putObjectArgs);
            return objectWriteResponse.object();
        } catch (ErrorResponseException
                 | InsufficientDataException
                 | InternalException
                 | InvalidKeyException
                 | InvalidResponseException
                 | IOException
                 | NoSuchAlgorithmException
                 | ServerException
                 | XmlParserException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ResultCode.ERROR);
        }
    }

}
