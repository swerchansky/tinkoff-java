package edu.java.service.ratelimit;

import edu.java.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {
    private final ApplicationConfig applicationConfig;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        ApplicationConfig.RateLimiter rateLimiter = applicationConfig.rateLimiter();
        boolean isEnabled = rateLimiter.enable();
        int limit = isEnabled ? rateLimiter.limit() : Integer.MAX_VALUE;
        int refill = isEnabled ? rateLimiter.refill() : Integer.MAX_VALUE;

        Bandwidth bandwidth = Bandwidth.builder()
            .capacity(limit)
            .refillIntervally(refill, Duration.ofMinutes(1))
            .initialTokens(limit)
            .build();

        return Bucket.builder().addLimit(bandwidth).build();
    }
}
