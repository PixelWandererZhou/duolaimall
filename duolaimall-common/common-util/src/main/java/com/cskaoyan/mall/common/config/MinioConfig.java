package com.cskaoyan.mall.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties("minio")
public class MinioConfig {
    private String endpointUrl;
    private String accessKey;
    private String secreKey;
    private String bucketName;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpointUrl)
                .credentials(accessKey, secreKey)
                .build();
    }
}