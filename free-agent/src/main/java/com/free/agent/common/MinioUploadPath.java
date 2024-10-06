package com.free.agent.common;

import lombok.Getter;

@Getter
public enum MinioUploadPath {

    BUCKET_NAME("free-agent"),

    /**
     * 注册上传文件类型（营业执照，身份证正反面）
     */
    REGISTER_FILE("doc_files/");

    private final String objectPath;

    MinioUploadPath(String objectPath) {
        this.objectPath = objectPath;
    }

}
