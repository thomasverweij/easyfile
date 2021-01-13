package dev.tho.easyfile.controller;


import dev.tho.easyfile.dto.BucketDto;
import dev.tho.easyfile.dto.CreateBucketDto;
import dev.tho.easyfile.dto.CreateFileDto;
import dev.tho.easyfile.dto.FileMetadataDto;
import dev.tho.easyfile.exception.EntityNotFoundException;
import dev.tho.easyfile.service.BucketService;
import dev.tho.easyfile.service.FileMetadataService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileMetadataService fileMetadataService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileMetadataDto> createFile(@RequestBody MultipartFile file,
                                                      Authentication auth) throws Exception {
        UUID id = UUID.fromString(auth.getPrincipal().toString());
        InputStream inputStream =  new BufferedInputStream(file.getInputStream());
        String fileName = file.getOriginalFilename();
        FileMetadataDto fileMetadataDto = fileMetadataService.create(inputStream, fileName, id);
        return new ResponseEntity<>(fileMetadataDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FileMetadataDto> getFile(@PathVariable("id") UUID fileId, Authentication auth) {
        UUID bucketId = UUID.fromString(auth.getPrincipal().toString());
        FileMetadataDto fileMetadataDto = fileMetadataService.getById(bucketId, fileId)
                .orElseThrow(EntityNotFoundException::new);
        return new ResponseEntity<>(fileMetadataDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") UUID fileId,  Authentication auth) throws Exception {
        UUID bucketId = UUID.fromString(auth.getPrincipal().toString());
        fileMetadataService.deleteById(bucketId, fileId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
