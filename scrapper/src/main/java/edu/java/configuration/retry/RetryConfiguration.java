package edu.java.configuration.retry;

import edu.java.client.exception.ApiErrorException;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
        if (throwable instanceof ApiErrorException exception) {
            Integer code = exception.response != null
                ? Integer.parseInt(exception.response.code)
                : HttpStatus.INTERNAL_SERVER_ERROR.value();
            return applicationConfig.retry().codes().contains(code);
        }
        return false;
    }
}
