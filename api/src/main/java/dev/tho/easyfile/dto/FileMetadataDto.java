package dev.tho.easyfile.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class FileMetadataDto {

    private UUID id;
    private Instant createdDate;
    private String fileName;
    private String url;

    public FileMetadataDto() {};

    public FileMetadataDto(UUID id, Instant createdDate, String fileName, String url) {
        this.id = id;
        this.createdDate = createdDate;
        this.fileName = fileName;
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadataDto that = (FileMetadataDto) o;
        return id.equals(that.id) &&
                createdDate.equals(that.createdDate) &&
                fileName.equals(that.fileName) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, fileName, url);
    }

    @Override
    public String toString() {
        return "FileMetadataDto{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
