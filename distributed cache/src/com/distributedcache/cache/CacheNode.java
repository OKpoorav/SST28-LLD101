package com.distributedcache.cache;

import com.distributedcache.model.CacheConfig;
import com.distributedcache.model.CacheEntry;
import com.distributedcache.observer.CacheObserver;
import com.distributedcache.observer.InvalidationObserver;
import com.distributedcache.strategy.EvictionPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheNode implements Cache {

    private String nodeId;
    private Map<String, CacheEntry> store;
    private EvictionPolicy evictionPolicy;
    private int maxSize;
    private int defaultTtlSeconds;
    private List<CacheObserver> observers;

    public CacheNode(String nodeId, CacheConfig config, EvictionPolicy policy) {
        this.nodeId = nodeId;
        this.store = new HashMap<>();
        this.evictionPolicy = policy;
        this.maxSize = config.getMaxSizePerNode();
        this.defaultTtlSeconds = config.getDefaultTtlSeconds();
        this.observers = new ArrayList<>();
    }

    public void put(String key, Object value) {
        put(key, value, defaultTtlSeconds);
    }

    public void put(String key, Object value, int ttlSeconds) {
        if (store.size() >= maxSize && !store.containsKey(key)) {
            String victim = evictionPolicy.pickEvictionCandidate(store);
            if (victim != null) {
                store.remove(victim);
                System.out.println("Node [" + nodeId + "]: evicted key [" + victim + "] to make room.");
            }
        }

        CacheEntry entry = new CacheEntry(key, value, ttlSeconds);
        evictionPolicy.onInsert(key);
        store.put(key, entry);
        System.out.println("Node [" + nodeId + "]: stored key [" + key + "].");

        for (CacheObserver observer : observers) {
            if (observer instanceof InvalidationObserver) {
                InvalidationObserver invalidationObserver = (InvalidationObserver) observer;
                if (invalidationObserver.getTargetNode().getNodeId().equals(this.nodeId)) {
                    continue;
                }
            }
            observer.onInvalidate(key);
        }
    }

    public Object get(String key) {
        if (!store.containsKey(key)) {
            return null;
        }

        CacheEntry entry = store.get(key);

        if (entry.isExpired()) {
            store.remove(key);
            System.out.println("Node [" + nodeId + "]: key [" + key + "] expired, removed.");
            return null;
        }

        entry.updateLastAccessed();
        evictionPolicy.onAccess(key);
        return entry.getValue();
    }

    public void evict(String key) {
        store.remove(key);
    }

    public void clear() {
        store.clear();
    }

    public void registerObserver(CacheObserver observer) {
        observers.add(observer);
    }

    public String getNodeId() {
        return nodeId;
    }

    public List<CacheObserver> getObservers() {
        return observers;
    }
}
