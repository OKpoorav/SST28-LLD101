package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowStrategy implements RateLimitStrategy {

    private RateLimiterConfig config;
    private Map<String, Integer> requestCounts;
    private Map<String, Long> windowStartTimes;

    public FixedWindowStrategy(RateLimiterConfig config) {
        this.config = config;
        this.requestCounts = new HashMap<>();
        this.windowStartTimes = new HashMap<>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        long now = System.currentTimeMillis();
        long windowMillis = config.getWindowSizeSeconds() * 1000L;

        if (!windowStartTimes.containsKey(clientId)) {
            windowStartTimes.put(clientId, now);
            requestCounts.put(clientId, 0);
        }

        long elapsed = now - windowStartTimes.get(clientId);
        if (elapsed >= windowMillis) {
            windowStartTimes.put(clientId, now);
            requestCounts.put(clientId, 0);
        }

        int currentCount = requestCounts.get(clientId);
        if (currentCount >= config.getMaxRequests()) {
            return false;
        }

        requestCounts.put(clientId, currentCount + 1);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}
