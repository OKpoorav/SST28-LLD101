package com.distributedcache.strategy;

import com.distributedcache.cache.CacheNode;

import java.util.List;

public interface DistributionStrategy {

    CacheNode getNodeForKey(String key, List<CacheNode> nodes);
}
