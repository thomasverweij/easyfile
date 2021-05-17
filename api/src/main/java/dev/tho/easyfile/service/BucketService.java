package dev.tho.easyfile.service;

import dev.tho.easyfile.dto.BucketDto;
import dev.tho.easyfile.exception.EntityNotFoundException;
import dev.tho.easyfile.mapper.BucketMapper;
import dev.tho.easyfile.model.Bucket;
import dev.tho.easyfile.repository.BucketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
public class BucketService implements UserDetailsService {

    @Autowired
    private BucketRepository bucketRepository;

    @Autowired
    private BucketMapper bucketMapper;

    @Autowired
    private S3Service s3Service;

    private Logger logger =  LoggerFactory.getLogger(this.getClass());


    public BucketDto create(String password) {
        Bucket bucket = new Bucket(password);
        bucket.setCreatedDate(Instant.now());
        logger.debug(bucket.toString());
        Bucket result = bucketRepository.save(bucket);
        return bucketMapper.asDto(result);
    }

    public Optional<BucketDto> getBucketById(UUID id) {
        try {
            Optional<Bucket> buckets = bucketRepository.findById(id);
            logger.debug(buckets.toString());
            return buckets
                    .stream()
                    .map(bucketMapper::asDto)
                    .findFirst();
        }
        catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException();
        }
    }

    public void deleteBucket(UUID id) throws Exception {
        try {
            bucketRepository.deleteById(id);
            s3Service.deleteBucket(id.toString());
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Bucket bucket = bucketRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UsernameNotFoundException(id));

        return new User(bucket.getId().toString(), bucket.getPassword(), emptyList());
    }
}
