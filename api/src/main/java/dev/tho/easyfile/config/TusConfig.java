package dev.tho.easyfile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.desair.tus.server.TusFileUploadService;

@Configuration
public class TusConfig {

    @Value("${tus.upload-url}")
    public String uploadUrl;

    public static String tusDir = "/tmp/easyfile/tus";

    @Bean
    public TusFileUploadService getTusFileUploadService() {
        return new TusFileUploadService()
                .withStoragePath(tusDir)
                .withUploadURI(uploadUrl);
    }
}
