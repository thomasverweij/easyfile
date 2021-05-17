package dev.tho.easyfile.repository;

import dev.tho.easyfile.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BucketRepository extends JpaRepository<Bucket, UUID> { }