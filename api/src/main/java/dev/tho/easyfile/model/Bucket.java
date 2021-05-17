package dev.tho.easyfile.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Bucket {

    @Id
    @GeneratedValue
    private UUID id;
//    private Instant createdDate;
    private String password;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FileMetadata> files;

    public Bucket() {}

    public Bucket(String password) {
        this.password = password;
        this.files = new ArrayList<>();
    };

    public Bucket(UUID id, String password, List<FileMetadata> files) {
        this.id = id;
        this.password = password;
        this.files = files;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FileMetadata> getFiles() {
        return files;
    }

    public void setFiles(List<FileMetadata> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bucket bucket = (Bucket) o;
        return id.equals(bucket.id) &&
                password.equals(bucket.password) &&
                files.equals(bucket.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, files);
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", files=" + files +
                '}';
    }
}
