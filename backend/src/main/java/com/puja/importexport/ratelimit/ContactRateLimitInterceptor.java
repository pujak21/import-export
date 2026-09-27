package com.puja.importexport.ratelimit;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Best-effort in-memory, per-client-IP fixed-window rate limiter for inquiry submissions.
 * State is per application instance and is reset on restart.
 */
@Component
public class ContactRateLimitInterceptor implements HandlerInterceptor {

    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long windowMillis;
    private final String clientIpHeader;
    private final Clock clock;

    public ContactRateLimitInterceptor(@Value("${app.rate-limit.contact.max-requests}") int maxRequests,
                                       @Value("${app.rate-limit.contact.window-seconds}") long windowSeconds,
                                       @Value("${app.rate-limit.client-ip-header:}") String clientIpHeader) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowSeconds * 1000;
        this.clientIpHeader = clientIpHeader;
        this.clock = Clock.systemUTC();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        long now = clock.millis();
        if (windows.size() > CLEANUP_THRESHOLD) {
            windows.values().removeIf(window -> window.isExpired(now, windowMillis));
        }

        Window window = windows.compute(resolveClientIp(request), (key, existing) ->
                existing == null || existing.isExpired(now, windowMillis)
                        ? new Window(now, 1)
                        : new Window(existing.start(), existing.count() + 1));

        if (window.count() > maxRequests) {
            long retryAfterMillis = window.start() + windowMillis - now;
            throw new RateLimitExceededException(Math.max(1, (retryAfterMillis + 999) / 1000));
        }
        return true;
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (clientIpHeader != null && !clientIpHeader.isBlank()) {
            String headerValue = request.getHeader(clientIpHeader);
            if (headerValue != null && !headerValue.isBlank()) {
                return headerValue.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    private record Window(long start, int count) {
        boolean isExpired(long now, long windowMillis) {
            return now - start >= windowMillis;
        }
    }
}
