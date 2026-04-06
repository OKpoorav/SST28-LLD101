package com.distributedcache.strategy;

import com.distributedcache.model.CacheEntry;

import java.util.Map;

public interface EvictionPolicy {

    void onAccess(String key);

    void onInsert(String key);

    String pickEvictionCandidate(Map<String, CacheEntry> store);
}
