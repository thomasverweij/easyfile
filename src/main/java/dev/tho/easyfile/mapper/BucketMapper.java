package dev.tho.easyfile.mapper;

import dev.tho.easyfile.dto.BucketDto;
import dev.tho.easyfile.model.Bucket;
import org.springframework.stereotype.Component;

@Component
public class BucketMapper {

    public BucketDto asDto(Bucket bucket) {
        BucketDto bucketDto = new BucketDto();
        bucketDto.setId(bucket.getId());
        bucketDto.setFiles(bucket.getFiles());
        return bucketDto;
    }
}
