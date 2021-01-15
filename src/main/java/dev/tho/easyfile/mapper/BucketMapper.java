package dev.tho.easyfile.mapper;

import dev.tho.easyfile.dto.BucketDto;
import dev.tho.easyfile.model.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BucketMapper {

    @Autowired
    FileMetadataMapper fileMetadataMapper;

    public BucketDto asDto(Bucket bucket) {
        BucketDto bucketDto = new BucketDto();
        bucketDto.setId(bucket.getId());
        bucketDto.setFiles(
                bucket.getFiles()
                        .stream()
                        .map(fileMetadataMapper::asDto)
                        .collect(Collectors.toList())
        );
        return bucketDto;
    }
}
