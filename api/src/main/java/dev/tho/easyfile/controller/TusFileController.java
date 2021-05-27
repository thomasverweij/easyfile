package dev.tho.easyfile.controller;


import dev.tho.easyfile.EasyfileApplication;
import dev.tho.easyfile.dto.FileMetadataDto;
import dev.tho.easyfile.service.FileMetadataService;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class TusFileController {

    private final TusFileUploadService tusFileUploadService;

    public static final String TUSDIR = "/tmp/easyfile/tus";

    @Autowired
    FileMetadataService fileMetadataService;

    public TusFileController() {
        this.tusFileUploadService = new TusFileUploadService()
                .withStoragePath(TUSDIR)
                .withUploadURI("/upload");

        Path tusUploadDirectory = Paths.get(TUSDIR);
    }

    @RequestMapping(value = { "/upload", "/upload/**" }, method = { RequestMethod.POST,
            RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.GET })
    public void upload(HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse, Authentication auth) throws Exception {
        this.tusFileUploadService.process(servletRequest, servletResponse);

        String uploadURI = servletRequest.getRequestURI();

        UploadInfo uploadInfo = null;
        try {
            uploadInfo = this.tusFileUploadService.getUploadInfo(uploadURI);
        }
        catch (IOException | TusException e) {
            EasyfileApplication.logger.warning("get upload info" + e);
        }

        if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
            try (InputStream fileStream = this.tusFileUploadService.getUploadedBytes(uploadURI)) {
                System.out.println(this.tusFileUploadService.getUploadInfo(uploadURI));
                String fileName = this.tusFileUploadService.getUploadInfo(uploadURI).getFileName();
                UUID id = UUID.fromString(auth.getPrincipal().toString());
                InputStream inputStream =  new BufferedInputStream(fileStream);
                fileMetadataService.create(inputStream, fileName, id);
            }
            catch (IOException | TusException e) {
                EasyfileApplication.logger.warning("get uploaded bytes" + e);
            }

            try {
                this.tusFileUploadService.deleteUpload(uploadURI);
            }
            catch (IOException | TusException e) {
                EasyfileApplication.logger.warning("delete upload" + e);
            }
        }
    }

    @Scheduled(fixedDelayString = "PT24H")
    private void cleanup() {
        Path locksDir = Paths.get(TUSDIR).resolve("locks");
        if (Files.exists(locksDir)) {
            try {
                this.tusFileUploadService.cleanup();
            }
            catch (IOException e) {
                EasyfileApplication.logger.warning("error during cleanup" + e);
            }
        }
    }

}