package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.HashMap;
import java.util.Map;

public class TokenBucketStrategy implements RateLimitStrategy {

    private RateLimiterConfig config;
    private Map<String, Double> tokenBuckets;
    private Map<String, Long> lastRefillTimes;

    public TokenBucketStrategy(RateLimiterConfig config) {
        this.config = config;
        this.tokenBuckets = new HashMap<>();
        this.lastRefillTimes = new HashMap<>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        long now = System.currentTimeMillis();

        if (!tokenBuckets.containsKey(clientId)) {
            tokenBuckets.put(clientId, (double) config.getMaxRequests());
            lastRefillTimes.put(clientId, now);
        }

        long elapsed = now - lastRefillTimes.get(clientId);
        double elapsedSeconds = elapsed / 1000.0;
        double tokensToAdd = elapsedSeconds * config.getRefillRatePerSecond();
        double newTokens = tokenBuckets.get(clientId) + tokensToAdd;

        if (newTokens > config.getMaxRequests()) {
            newTokens = config.getMaxRequests();
        }

        tokenBuckets.put(clientId, newTokens);
        lastRefillTimes.put(clientId, now);

        if (newTokens < 1.0) {
            return false;
        }

        tokenBuckets.put(clientId, newTokens - 1.0);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}
