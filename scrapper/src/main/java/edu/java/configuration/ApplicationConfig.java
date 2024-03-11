package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    BaseUrls baseUrls,
    @NotNull
    Scheduler scheduler
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
}
