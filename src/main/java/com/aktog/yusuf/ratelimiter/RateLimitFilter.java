package com.aktog.yusuf.ratelimiter;


import com.aktog.yusuf.ratelimiter.model.RateLimitHeader;
import com.aktog.yusuf.ratelimiter.model.RequestInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class RateLimitFilter implements Filter {

    private final RateLimitConfigurer configurer;
    private final Map<String, RequestInfo> ipRequests = new ConcurrentHashMap<>();

    public RateLimitFilter(RateLimitConfigurer configurer) {
        this.configurer = configurer;
    }

    @Override
    public final void doFilter(ServletRequest servletRequest,
                               ServletResponse servletResponse,
                               FilterChain filterChain) throws IOException, ServletException {
        final long now = Instant.now().toEpochMilli();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String key = configurer.getRateLimitKey(request);

        RequestInfo requestInfo = computeRequestInfo(key, now);

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        RateLimitHeader responseHeader = RateLimitHeader.construct(
                configurer.configureRateLimitByKey(key),
                requestInfo.count(),
                configurer.configureRateLimitDurationByKey(key),
                requestInfo.timestamp());

        if (configurer.enableRateLimitResponseHeaders()) {
            prepareHttpResponse(response, responseHeader);
        }

        if (requestInfo.count() <= configurer.configureRateLimitByKey(key)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }

        cleanUpExpiredRequests(now);
    }


    private void prepareHttpResponse(HttpServletResponse response, RateLimitHeader rateLimitHeader) {

        response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimitHeader.rateLimit()));
        response.setHeader("X-RateLimit-Duration", rateLimitHeader.duration().toString());
        response.setHeader("X-RateLimit-Remaining", String.valueOf(rateLimitHeader.remainingLimits()));
        response.setHeader("X-RateLimit-Reset", rateLimitHeader.requestLimitRefreshDate());
    }

    private RequestInfo computeRequestInfo(String key, long now) {
        return ipRequests.compute(key, (_, info) -> {
            if (info == null || now - info.timestamp() > configurer.configureRateLimitDurationByKey(key).toMillis()) {
                return new RequestInfo(1, now);
            } else {
                int count = Math.min(info.count() + 1, configurer.configureRateLimit() + 1);
                return new RequestInfo(count, info.timestamp());
            }
        });

    }

    private void cleanUpExpiredRequests(long now) {
        ipRequests.entrySet().removeIf(entry ->
                now - entry.getValue().timestamp() > configurer.configureRateLimitDurationByKey(entry.getKey()).toMillis()
        );
    }

}
