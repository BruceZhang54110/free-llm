package com.free.agent.service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bruce
 */
public interface DocumentService {

    /**
     * 上传文件到minio
     * @param uploadFile uploadFile
     * @return 上传成功
     */
    boolean uploadFileToMinio(MultipartFile uploadFile);

    /**
     * After the file is successfully uploaded, the data needs to be inserted into the database
     * @param fileName upload file name
     * @param filePath The file path generated after successful upload
     * @return Is the operation successful
     */
    boolean insertUploadFileData(Long fileSize, String fileName, String filePath);

}
