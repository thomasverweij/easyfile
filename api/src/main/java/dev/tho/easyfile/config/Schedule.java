package dev.tho.easyfile.config;

import dev.tho.easyfile.model.Bucket;
import dev.tho.easyfile.repository.BucketRepository;
import dev.tho.easyfile.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@EnableScheduling
public class Schedule {

    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    BucketService bucketService;

    @Transactional
    @Scheduled(fixedDelay = 300000)
    public void scheduleFixedDelayTask() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        List<Bucket> buckets = bucketRepository.findAllByCreatedDateBefore(yesterday);
        System.out.println(buckets);
        buckets.forEach(b -> {
            try {
                bucketService.deleteBucket(b.getId());
                System.out.println("Deleted bucket: "+ b.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
