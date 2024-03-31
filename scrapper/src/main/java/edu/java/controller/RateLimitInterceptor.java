package edu.java.controller;

import edu.java.controller.exception.TooManyRequestException;
import edu.java.service.ratelimit.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final String API_HEADER = "X-Forwarded-For";
    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) {
        String ip = extractIp(request);

        Bucket bucket = rateLimitService.resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            return true;
        } else {
            throw new TooManyRequestException();
        }
    }

    private String extractIp(HttpServletRequest request) {
        String ip = request.getHeader(API_HEADER);
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}
