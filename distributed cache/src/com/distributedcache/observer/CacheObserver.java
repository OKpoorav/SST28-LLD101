package com.distributedcache.observer;

public interface CacheObserver {

    void onInvalidate(String key);

    void onNodeAdded(String nodeId);

    void onNodeRemoved(String nodeId);
}
