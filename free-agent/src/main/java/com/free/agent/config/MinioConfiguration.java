package com.free.agent.config;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
@Configuration
public class MinioConfiguration {

    private String url;

    private String accessKey;

    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(url)
                .credentials(accessKey, secretKey).build();
    }

    public static PutObjectArgs.Builder buildPutObjectArgs(String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalArgumentException("bucketName is empty.");
        }
        PutObjectArgs.Builder object = PutObjectArgs.builder()
                .bucket(bucketName);
        return object;
    }
}
