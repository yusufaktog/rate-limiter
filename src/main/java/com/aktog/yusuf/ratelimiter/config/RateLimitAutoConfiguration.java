package com.aktog.yusuf.ratelimiter.config;

import com.aktog.yusuf.ratelimiter.RateLimitConfigurer;
import com.aktog.yusuf.ratelimiter.RateLimitFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(RateLimitConfigurer.class)
public class RateLimitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "rateLimitFilterRegistration")
    public FilterRegistrationBean<Filter> rateLimitFilterRegistration(RateLimitConfigurer configurer) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter(configurer));
        registration.addUrlPatterns("/*");
        //registration.setOrder(1);
        return registration;
    }
}
