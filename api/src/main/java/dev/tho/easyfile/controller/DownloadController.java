package dev.tho.easyfile.controller;

import dev.tho.easyfile.dto.FileMetadataDto;
import dev.tho.easyfile.exception.EntityNotFoundException;
import dev.tho.easyfile.service.FileMetadataService;
import dev.tho.easyfile.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    S3Service s3Service;

    @Autowired
    FileMetadataService fileMetadataService;

    @GetMapping
    public ResponseEntity<InputStreamResource> download(@RequestParam UUID fileId, Authentication auth) throws Exception {
        UUID bucketId = UUID.fromString(auth.getPrincipal().toString());
        FileMetadataDto fileMetadata = fileMetadataService.getById(bucketId, fileId)
                .orElseThrow(EntityNotFoundException::new);
        InputStream file = s3Service.getFile(bucketId.toString(), fileMetadata.getId().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileMetadata.getFileName());
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));
        InputStreamResource inputStreamResource = new InputStreamResource(file);
        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

}
