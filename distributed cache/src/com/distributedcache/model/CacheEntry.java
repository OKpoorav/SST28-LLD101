package com.distributedcache.model;

public class CacheEntry {

    private String key;
    private Object value;
    private long expiresAtMillis;
    private long lastAccessedMillis;

    public CacheEntry(String key, Object value, int ttlSeconds) {
        this.key = key;
        this.value = value;
        this.expiresAtMillis = System.currentTimeMillis() + (ttlSeconds * 1000L);
        this.lastAccessedMillis = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAtMillis;
    }

    public void updateLastAccessed() {
        this.lastAccessedMillis = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }

    public long getLastAccessedMillis() {
        return lastAccessedMillis;
    }
}
