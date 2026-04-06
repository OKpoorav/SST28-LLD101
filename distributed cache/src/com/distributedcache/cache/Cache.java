package com.distributedcache.cache;

public interface Cache {

    void put(String key, Object value);

    void put(String key, Object value, int ttlSeconds);

    Object get(String key);

    void evict(String key);

    void clear();
}
