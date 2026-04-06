package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.HashMap;
import java.util.Map;

public class LeakyBucketStrategy implements RateLimitStrategy {

    private RateLimiterConfig config;
    private Map<String, Integer> queueSizes;
    private Map<String, Long> lastLeakTimes;

    public LeakyBucketStrategy(RateLimiterConfig config) {
        this.config = config;
        this.queueSizes = new HashMap<>();
        this.lastLeakTimes = new HashMap<>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        long now = System.currentTimeMillis();

        if (!queueSizes.containsKey(clientId)) {
            queueSizes.put(clientId, 0);
            lastLeakTimes.put(clientId, now);
        }

        long elapsed = now - lastLeakTimes.get(clientId);
        double elapsedSeconds = elapsed / 1000.0;
        int leaked = (int) (elapsedSeconds * config.getRefillRatePerSecond());

        if (leaked > 0) {
            int currentSize = queueSizes.get(clientId) - leaked;
            if (currentSize < 0) {
                currentSize = 0;
            }
            queueSizes.put(clientId, currentSize);
            lastLeakTimes.put(clientId, now);
        }

        if (queueSizes.get(clientId) >= config.getMaxRequests()) {
            return false;
        }

        queueSizes.put(clientId, queueSizes.get(clientId) + 1);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}
