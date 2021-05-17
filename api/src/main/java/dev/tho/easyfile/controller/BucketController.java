package dev.tho.easyfile.controller;

import dev.tho.easyfile.dto.BucketDto;
import dev.tho.easyfile.dto.CreateBucketDto;
import dev.tho.easyfile.exception.BucketNotFoundException;
import dev.tho.easyfile.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/bucket")
public class BucketController {

    @Autowired
    BucketService bucketService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public ResponseEntity<BucketDto> createBucket(@Valid @RequestBody CreateBucketDto createBucketDto) {
        BucketDto bucketDto = bucketService.create(bCryptPasswordEncoder.encode(createBucketDto.getPassword()));
        return new ResponseEntity<>(bucketDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BucketDto> getBucket(Authentication auth) {
        UUID id = UUID.fromString(auth.getPrincipal().toString());
        BucketDto bucketDto = bucketService.getBucketById(id).orElseThrow(BucketNotFoundException::new);
        return new ResponseEntity<>(bucketDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBucket(Authentication auth) throws Exception {
        UUID id = UUID.fromString(auth.getPrincipal().toString());
        bucketService.deleteBucket(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
