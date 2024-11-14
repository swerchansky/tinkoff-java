package edu.java.configuration;

import edu.java.configuration.retry.BackoffPolicy;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    BaseUrls baseUrls,
    @NotNull
    Scheduler scheduler,
    AccessType databaseAccessType,
    RateLimiter rateLimiter,
    Retry retry,
    Topic topic,
    boolean useQueue
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record BaseUrls(
        @NotEmpty
        @DefaultValue("https://api.github.com")
        String github,
        @NotEmpty
        @DefaultValue("https://api.stackexchange.com/2.3")
        String stackOverflow,
        @NotEmpty
        @DefaultValue("http//localhost:8090")
        String bot
    ) {
    }

    public record RateLimiter(
        boolean enable,
        @NotNull Integer limit,
        @NotNull Integer refill
    ) {
    }

    public record Retry(
        @NotNull BackoffPolicy backoffPolicy,
        @NotNull Integer attempts,
        @NotNull Duration delay,
        @NotNull Double jitter,
        Set<Integer> codes
    ) {
    }

    public record Topic(
        @NotNull String name,
        @NotNull Integer partitions,
        @NotNull Integer replicas
    ) {
    }
}
