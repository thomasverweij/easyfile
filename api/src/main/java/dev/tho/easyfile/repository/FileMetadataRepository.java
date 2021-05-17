package dev.tho.easyfile.repository;

import dev.tho.easyfile.model.Bucket;
import dev.tho.easyfile.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {}