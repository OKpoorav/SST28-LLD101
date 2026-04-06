package com.distributedcache.model;

public class CacheConfig {

    private int maxSizePerNode;
    private int defaultTtlSeconds;

    public CacheConfig(int maxSizePerNode, int defaultTtlSeconds) {
        this.maxSizePerNode = maxSizePerNode;
        this.defaultTtlSeconds = defaultTtlSeconds;
    }

    public int getMaxSizePerNode() {
        return maxSizePerNode;
    }

    public int getDefaultTtlSeconds() {
        return defaultTtlSeconds;
    }
}
