package com.innovatech.inventory.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("https://your-minio-endpoint.com")
                .credentials("your-access-key", "your-secret-key")
                .build();
    }
}
