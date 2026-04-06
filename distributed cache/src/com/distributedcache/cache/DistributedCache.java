package com.distributedcache.cache;

import com.distributedcache.db.DatabaseLoader;
import com.distributedcache.exception.NodeNotFoundException;
import com.distributedcache.model.CacheConfig;
import com.distributedcache.observer.CacheObserver;
import com.distributedcache.observer.InvalidationObserver;
import com.distributedcache.strategy.DistributionStrategy;

import java.util.ArrayList;
import java.util.List;

public class DistributedCache implements Cache {

    private List<CacheNode> nodes;
    private DistributionStrategy distributionStrategy;
    private DatabaseLoader databaseLoader;
    private CacheConfig config;

    public DistributedCache(CacheConfig config, DistributionStrategy strategy, DatabaseLoader loader) {
        this.config = config;
        this.distributionStrategy = strategy;
        this.databaseLoader = loader;
        this.nodes = new ArrayList<>();
    }

    public void addNode(CacheNode node) {
        for (CacheNode existingNode : nodes) {
            existingNode.registerObserver(new InvalidationObserver(node));
            node.registerObserver(new InvalidationObserver(existingNode));
        }

        nodes.add(node);
        System.out.println("DistributedCache: node [" + node.getNodeId() + "] added. Total nodes: [" + nodes.size() + "].");

        for (CacheNode currentNode : nodes) {
            for (CacheObserver observer : currentNode.getObservers()) {
                observer.onNodeAdded(node.getNodeId());
            }
        }
    }

    public void removeNode(CacheNode node) {
        nodes.remove(node);
        System.out.println("DistributedCache: node [" + node.getNodeId() + "] removed.");
    }

    public void put(String key, Object value) {
        put(key, value, config.getDefaultTtlSeconds());
    }

    public void put(String key, Object value, int ttlSeconds) {
        if (nodes.isEmpty()) {
            throw new NodeNotFoundException("No nodes in cache.");
        }

        CacheNode node = distributionStrategy.getNodeForKey(key, nodes);
        node.put(key, value, ttlSeconds);
    }

    public Object get(String key) {
        if (nodes.isEmpty()) {
            throw new NodeNotFoundException("No nodes in cache.");
        }

        CacheNode node = distributionStrategy.getNodeForKey(key, nodes);
        Object result = node.get(key);

        if (result == null) {
            System.out.println("DistributedCache: cache miss for key [" + key + "], loading from DB.");
            Object loadedValue = databaseLoader.load(key);
            if (loadedValue != null) {
                node.put(key, loadedValue, config.getDefaultTtlSeconds());
            }
            return loadedValue;
        }

        return result;
    }

    public void evict(String key) {
        for (CacheNode node : nodes) {
            node.evict(key);
        }
    }

    public void clear() {
        for (CacheNode node : nodes) {
            node.clear();
        }
    }
}
