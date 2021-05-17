package dev.tho.easyfile.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;

@Entity
public class FileMetadata {

    @Id
    @GeneratedValue
    private UUID id;
    private Instant createdDate;
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    public FileMetadata() {}

    public FileMetadata(UUID id, Instant createdDate, String fileName) {
        this.id = id;
        this.createdDate = createdDate;
        this.fileName = fileName;
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

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() { return bucket; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return id.equals(that.id) &&
                createdDate.equals(that.createdDate) &&
                fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, fileName);
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}