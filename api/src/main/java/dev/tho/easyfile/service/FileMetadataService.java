package dev.tho.easyfile.service;

import dev.tho.easyfile.dto.FileMetadataDto;
import dev.tho.easyfile.exception.EntityNotFoundException;
import dev.tho.easyfile.mapper.FileMetadataMapper;
import dev.tho.easyfile.model.Bucket;
import dev.tho.easyfile.model.FileMetadata;
import dev.tho.easyfile.repository.BucketRepository;
import dev.tho.easyfile.repository.FileMetadataRepository;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileMetadataService {

    @Autowired
    FileMetadataRepository fileMetadataRepository;

    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    FileMetadataMapper fileMetadataMapper;

    @Autowired
    S3Service s3Service;

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    public FileMetadataDto create(InputStream fileContent, String fileName, UUID bucketId) throws Exception {
        Bucket bucket = bucketRepository.getOne(bucketId);
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setCreatedDate(Instant.now());
        fileMetadata.setBucket(bucket);
        fileMetadata.setFileName(fileName);
        FileMetadata result = fileMetadataRepository.save(fileMetadata);
        s3Service.uploadFile(bucketId.toString(), (BufferedInputStream) fileContent, result.getId().toString());
        return fileMetadataMapper.asDto(result);
    }

    public Optional<FileMetadataDto> getById(UUID bucketId, UUID fileId) {
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(EntityNotFoundException::new);
        return bucket.getFiles()
                .stream()
                .filter(f -> f.getId().equals(fileId))
                .map(fileMetadataMapper::asDto)
                .findFirst();
    }

    public void deleteById(UUID bucketId, UUID fileId) throws Exception {
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(EntityNotFoundException::new);
        FileMetadata file = bucket.getFiles()
                .stream()
                .filter(f -> f.getId().equals(fileId))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        bucket.getFiles().remove(file);
        bucketRepository.save(bucket);
        s3Service.deleteFile(bucketId.toString(), fileId.toString());
    }

}
