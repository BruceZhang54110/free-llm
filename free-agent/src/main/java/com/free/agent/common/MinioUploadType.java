package com.free.agent.common;

public enum MinioUploadType {

    /**
     * 注册上传文件类型（营业执照，身份证正反面）
     */
    REGISTER_FILE("doc_files/");

    private String objectPath;

    MinioUploadType(String objectPath) {
        this.objectPath = objectPath;
    }

    public String getObjectPath() {
        return objectPath;
    }
}
