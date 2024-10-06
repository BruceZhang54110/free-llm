package com.free.agent.service.impl;

import com.free.agent.common.MinioUploadPath;
import com.free.agent.db.domain.UploadFiles;
import com.free.agent.db.service.UploadFilesService;
import com.free.agent.service.DocumentService;
import com.free.agent.service.MinioService;
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
        String uploadFilePath = minioService.uploadMultipartFile(uploadFile, MinioUploadPath.REGISTER_FILE);
        return insertUploadFileData(uploadFile.getSize(), uploadFile.getOriginalFilename(), uploadFilePath);
    }

    @Override
    public boolean insertUploadFileData(Long fileSize, String fileName, String filePath) {
        UploadFiles insert = new UploadFiles();
        insert.setCreateTime(new Date());
        insert.setFileName(fileName);
        insert.setFilePath(filePath);
        insert.setFileSize(fileSize);
        return uploadFilesService.save(insert);
    }
}
