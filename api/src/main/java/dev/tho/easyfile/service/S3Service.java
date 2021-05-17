package dev.tho.easyfile.service;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Service
public class S3Service {

    @Autowired
    MinioClient minioClient;

    public void uploadFile(String bucketId, BufferedInputStream content, String fileId) throws Exception {
        try {
            boolean found =
                    minioClient.bucketExists(
                            BucketExistsArgs.builder().bucket(bucketId).build()
                    );
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketId).build()
                );
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketId).object(fileId).stream(
                            content, content.available(), -1)
                            .build()
            );
        } catch (Exception e) {
            throw e;
        }
    }

    public InputStream getFile(String bucketId, String fileId) throws Exception {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketId).object(fileId).build()
            );
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void deleteFile(String bucketId, String fileId) throws Exception {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketId).build());
            if (found) {
                minioClient.removeObject(
                        RemoveObjectArgs
                                .builder()
                                .bucket(bucketId)
                                .object(fileId)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void deleteBucket(String bucketId) throws Exception {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketId).build());
            if (found) {
                Iterable<Result<Item>> results = minioClient.listObjects(
                        ListObjectsArgs.builder().bucket(bucketId).build()
                );
                for (Result<Item> result : results) {
                    minioClient.removeObject(
                            RemoveObjectArgs
                                    .builder()
                                    .bucket(bucketId)
                                    .object(result.get().objectName())
                                    .build());
                }
                minioClient.removeBucket(
                        RemoveBucketArgs.builder().bucket(bucketId).build()
                );
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

}
