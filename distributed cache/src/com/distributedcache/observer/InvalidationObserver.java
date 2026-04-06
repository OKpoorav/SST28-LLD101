package com.distributedcache.observer;

import com.distributedcache.cache.CacheNode;

public class InvalidationObserver implements CacheObserver {

    private CacheNode targetNode;

    public InvalidationObserver(CacheNode targetNode) {
        this.targetNode = targetNode;
    }

    public void onInvalidate(String key) {
        System.out.println("Observer: invalidating key [" + key + "] on node [" + targetNode.getNodeId() + "]");
        targetNode.evict(key);
    }

    public void onNodeAdded(String nodeId) {
        System.out.println("Observer: node [" + nodeId + "] added.");
    }

    public void onNodeRemoved(String nodeId) {
        System.out.println("Observer: node [" + nodeId + "] removed.");
    }

    public CacheNode getTargetNode() {
        return targetNode;
    }
}
