package dev.tho.easyfile.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${minio.accesskey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.endpointUrl}")
    private String endpointUrl;

    @Bean
    public MinioClient getS3Client() {
        return MinioClient.builder()
                .endpoint(endpointUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
