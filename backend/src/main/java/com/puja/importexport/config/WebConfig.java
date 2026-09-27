package com.puja.importexport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.puja.importexport.ratelimit.ContactRateLimitInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;
    private final ContactRateLimitInterceptor contactRateLimitInterceptor;

    public WebConfig(@Value("${app.cors.allowed-origins}") String[] allowedOrigins,
                     ContactRateLimitInterceptor contactRateLimitInterceptor) {
        this.allowedOrigins = allowedOrigins;
        this.contactRateLimitInterceptor = contactRateLimitInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("POST", "OPTIONS")
                .allowedHeaders("Content-Type");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(contactRateLimitInterceptor).addPathPatterns("/api/contact");
    }
}
