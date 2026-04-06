package com.distributedcache.strategy;

import com.distributedcache.cache.CacheNode;
import com.distributedcache.exception.NodeNotFoundException;

import java.util.List;

public class ModuloDistributionStrategy implements DistributionStrategy {

    public CacheNode getNodeForKey(String key, List<CacheNode> nodes) {
        if (nodes.isEmpty()) {
            throw new NodeNotFoundException("No cache nodes available.");
        }

        int index = Math.abs(key.hashCode()) % nodes.size();
        return nodes.get(index);
    }
}
