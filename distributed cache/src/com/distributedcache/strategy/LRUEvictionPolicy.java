package com.distributedcache.strategy;

import com.distributedcache.model.CacheEntry;

import java.util.Map;

public class LRUEvictionPolicy implements EvictionPolicy {

    public void onAccess(String key) {
    }

    public void onInsert(String key) {
    }

    public String pickEvictionCandidate(Map<String, CacheEntry> store) {
        if (store.isEmpty()) {
            return null;
        }

        String leastRecentlyUsedKey = null;
        long oldestAccessTime = Long.MAX_VALUE;

        for (Map.Entry<String, CacheEntry> entry : store.entrySet()) {
            if (entry.getValue().getLastAccessedMillis() < oldestAccessTime) {
                oldestAccessTime = entry.getValue().getLastAccessedMillis();
                leastRecentlyUsedKey = entry.getKey();
            }
        }

        return leastRecentlyUsedKey;
    }
}
