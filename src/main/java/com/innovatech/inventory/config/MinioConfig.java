package com.innovatech.inventory.config;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfig {

    @Value("${minio.url:NOT_FOUND}")
    private String minioUrl;

    @PostConstruct
    public void init() {
        System.out.println("MinIO URL: " + minioUrl);
    }

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String secretKey;

    @Bean
    @Primary
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl) // Usar el valor correcto desde las propiedades
                .build();
    }
}