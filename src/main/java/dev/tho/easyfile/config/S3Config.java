package dev.tho.easyfile.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${minio.accesskey:minio}")
    private String accessKey;

    @Value("${minio.secretKey:minio123}")
    private String secretKey;

    @Value("${minio.endpointUrl:localhost:9000}")
    private String endpointUrl;

    @Bean
    public MinioClient getS3Client() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minio", "minio123")
                .build();
    }
}
