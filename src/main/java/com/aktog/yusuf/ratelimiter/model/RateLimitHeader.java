package com.aktog.yusuf.ratelimiter.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record RateLimitHeader(int rateLimit, int remainingLimits, Duration duration, String requestLimitRefreshDate) {

    public static RateLimitHeader construct(int rateLimit, int count, Duration duration, Long firstRequestTime) {
        String isoTime = Instant.ofEpochMilli(firstRequestTime).toString();
        Instant instant = Instant.parse(isoTime);
        final LocalDateTime nextPossibleRequestTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).plus(duration);
        int remainingLimits = Math.max(rateLimit - count, 0);

        return new RateLimitHeader(rateLimit, remainingLimits, duration, nextPossibleRequestTime.toString());
    }
}

