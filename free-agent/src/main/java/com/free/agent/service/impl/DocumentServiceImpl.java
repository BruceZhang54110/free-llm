package com.free.agent.service.impl;

import com.free.agent.common.MinioUploadPath;
import com.free.agent.db.domain.UploadFiles;
import com.free.agent.db.service.UploadFilesService;
import com.free.agent.service.DocumentService;
import com.free.agent.service.MinioService;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final MinioService minioService;

    private final UploadFilesService uploadFilesService;

    /**
     * 上传文件到minio
     *
     * @param uploadFile uploadFile
     * @return 上传成功
     */
    @Override
    public boolean uploadFileToMinio(MultipartFile uploadFile) {
        ObjectWriteResponse objectWriteResponse = minioService.uploadMultipartFile(uploadFile, MinioUploadPath.REGISTER_FILE);
        return insertUploadFileData(uploadFile.getSize(), uploadFile.getOriginalFilename(), objectWriteResponse);
    }

    @Override
    public boolean insertUploadFileData(Long fileSize, String fileName, ObjectWriteResponse objectWriteResponse) {
        UploadFiles insert = new UploadFiles();
        insert.setCreateTime(new Date());
        insert.setFileName(fileName);
        insert.setFilePath(objectWriteResponse.object());
        insert.setBucketName(objectWriteResponse.bucket());
        insert.setFileSize(fileSize);
        return uploadFilesService.save(insert);
    }
}
