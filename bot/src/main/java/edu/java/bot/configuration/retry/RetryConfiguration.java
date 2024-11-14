package edu.java.bot.configuration.retry;

import edu.java.bot.client.exception.ApiErrorException;
import edu.java.bot.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
@RequiredArgsConstructor
public class RetryConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.backoffPolicy", havingValue = "fixed")
    public Retry fixedRetry() {
        ApplicationConfig.Retry retryConfig = applicationConfig.retry();
        return Retry.fixedDelay(retryConfig.attempts(), retryConfig.delay())
            .filter(this::filter)
            .jitter(retryConfig.jitter())
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.backoffPolicy", havingValue = "linear")
    public Retry linearRetry() {
        ApplicationConfig.Retry retryConfig = applicationConfig.retry();
        return new LinearRetry(retryConfig.attempts(), retryConfig.delay())
            .filter(this::filter)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "retry.backoffPolicy", havingValue = "exponential")
    public Retry exponentialRetry() {
        ApplicationConfig.Retry retryConfig = applicationConfig.retry();
        Retry.max(0);
        return Retry.backoff(retryConfig.attempts(), retryConfig.delay())
            .filter(this::filter)
            .jitter(retryConfig.jitter())
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    private boolean filter(Throwable throwable) {
        return throwable instanceof ApiErrorException exception
            && applicationConfig.retry()
            .codes()
            .contains(Integer.valueOf(exception.response.code));
    }
}
