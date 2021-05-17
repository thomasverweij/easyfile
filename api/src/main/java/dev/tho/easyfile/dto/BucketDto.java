package dev.tho.easyfile.dto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BucketDto {
    private UUID id;
    private Instant createdDate;
    private List<FileMetadataDto> files;

    public BucketDto() {};

    public BucketDto(UUID id, Instant createdDate, List<FileMetadataDto> files) {
        this.id = id;
        this.createdDate = createdDate;
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketDto bucketDto = (BucketDto) o;
        return id.equals(bucketDto.id) && createdDate.equals(bucketDto.createdDate) && files.equals(bucketDto.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, files);
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
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
    public String toString() {
        return "BucketDto{" +
                "id=" + id +
                ", files=" + files +
                '}';
    }
}
