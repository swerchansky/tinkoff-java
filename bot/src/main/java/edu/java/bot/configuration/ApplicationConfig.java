package edu.java.bot.configuration;

import edu.java.bot.configuration.retry.BackoffPolicy;
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
    @NotEmpty
    String telegramToken,
    BaseUrls baseUrls,
    RateLimiter rateLimiter,
    Retry retry,
    Topic topic
) {
    public record BaseUrls(
        @NotEmpty
        @DefaultValue("https://localhost:8080")
        String scrapper
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
