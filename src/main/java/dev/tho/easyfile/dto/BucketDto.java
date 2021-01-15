package dev.tho.easyfile.dto;

import dev.tho.easyfile.model.FileMetadata;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BucketDto {
    private UUID id;
    private List<FileMetadataDto> files;

    public BucketDto() {};

    public BucketDto(UUID id, List<FileMetadataDto> files) {
        this.id = id;
        this.files = files;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<FileMetadataDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileMetadataDto> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketDto bucketDto = (BucketDto) o;
        return id.equals(bucketDto.id) &&
                files.equals(bucketDto.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, files);
    }

    @Override
    public String toString() {
        return "BucketDto{" +
                "id=" + id +
                ", files=" + files +
                '}';
    }
}
