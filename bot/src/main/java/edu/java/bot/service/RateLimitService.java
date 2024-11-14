package edu.java.bot.service;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String ip);
}
