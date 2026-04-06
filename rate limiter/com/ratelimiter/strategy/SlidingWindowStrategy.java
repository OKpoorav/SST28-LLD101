package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SlidingWindowStrategy implements RateLimitStrategy {

    private RateLimiterConfig config;
    private Map<String, List<Long>> requestTimestamps;

    public SlidingWindowStrategy(RateLimiterConfig config) {
        this.config = config;
        this.requestTimestamps = new HashMap<>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        if (!requestTimestamps.containsKey(clientId)) {
            requestTimestamps.put(clientId, new ArrayList<>());
        }

        long now = System.currentTimeMillis();
        long windowMillis = config.getWindowSizeSeconds() * 1000L;

        List<Long> timestamps = requestTimestamps.get(clientId);
        Iterator<Long> iterator = timestamps.iterator();
        while (iterator.hasNext()) {
            long timestamp = iterator.next();
            if (now - timestamp > windowMillis) {
                iterator.remove();
            }
        }

        if (timestamps.size() >= config.getMaxRequests()) {
            return false;
        }

        timestamps.add(now);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}
