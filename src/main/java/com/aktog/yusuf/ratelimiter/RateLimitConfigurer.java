package com.aktog.yusuf.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;

public interface RateLimitConfigurer {

    Duration configureRateLimitDuration();

    int configureRateLimit();

    default String getRateLimitKey(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    default Duration configureRateLimitDurationByKey(String key) {
        return configureRateLimitDuration();
    }

    default int configureRateLimitByKey(String key) {
        return configureRateLimit();
    }

    default boolean enableRateLimitResponseHeaders(){
        return true;
    }

    default int getCurrentCount(String key){
        return 0;
    }
}
