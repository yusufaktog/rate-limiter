package com.aktog.yusuf.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Configo implements RateLimitConfigurer {
    @Override
    public Duration configureRateLimitDuration() {
        return Duration.ofMinutes(1);
    }

    @Override
    public int configureRateLimit() {
        return 10;
    }

    @Override
    public String getRateLimitKey(HttpServletRequest request) {
        return request.getRemoteAddr() + ":" + request.getRequestURI();
    }

    @Override
    public int configureRateLimitByKey(String key) {
        return key.contains("a") ? 9 : 8;
    }
}
