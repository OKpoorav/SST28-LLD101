package com.ratelimiter.model;

public class RateLimiterConfig {

    private int maxRequests;
    private int windowSizeSeconds;
    private int refillRatePerSecond;

    public RateLimiterConfig(int maxRequests, int windowSizeSeconds, int refillRatePerSecond) {
        this.maxRequests = maxRequests;
        this.windowSizeSeconds = windowSizeSeconds;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowSizeSeconds() {
        return windowSizeSeconds;
    }

    public int getRefillRatePerSecond() {
        return refillRatePerSecond;
    }
}
