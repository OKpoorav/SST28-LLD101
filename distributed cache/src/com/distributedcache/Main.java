package com.distributedcache;

import com.distributedcache.cache.Cache;
import com.distributedcache.cache.CacheNode;
import com.distributedcache.cache.DistributedCache;
import com.distributedcache.db.DatabaseLoader;
import com.distributedcache.model.CacheConfig;
import com.distributedcache.strategy.DistributionStrategy;
import com.distributedcache.strategy.EvictionPolicy;
import com.distributedcache.strategy.LRUEvictionPolicy;
import com.distributedcache.strategy.ModuloDistributionStrategy;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CacheConfig config = new CacheConfig(3, 60);
        DatabaseLoader db = new DatabaseLoader();
        DistributionStrategy strategy = new ModuloDistributionStrategy();
        Cache cache = new DistributedCache(config, strategy, db);

        EvictionPolicy lru = new LRUEvictionPolicy();
        CacheNode node1 = new CacheNode("node-1", config, lru);
        CacheNode node2 = new CacheNode("node-2", config, lru);
        CacheNode node3 = new CacheNode("node-3", config, lru);

        ((DistributedCache) cache).addNode(node1);
        ((DistributedCache) cache).addNode(node2);
        ((DistributedCache) cache).addNode(node3);

        System.out.println("\n--- Step 1: Basic put and get ---");
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        System.out.println("Get user:1 = " + cache.get("user:1"));
        System.out.println("Get user:2 = " + cache.get("user:2"));

        System.out.println("\n--- Step 2: Cache miss triggers DB load ---");
        System.out.println("Get user:3 (first call) = " + cache.get("user:3"));
        System.out.println("Get user:3 (second call, should be cached) = " + cache.get("user:3"));

        System.out.println("\n--- Step 3: Eviction (LRU) ---");
        cache.put("a", "value-a");
        cache.put("b", "value-b");
        cache.put("c", "value-c");
        cache.put("d", "value-d");
        cache.put("e", "value-e");
        cache.put("f", "value-f");
        cache.put("g", "value-g");
        cache.put("h", "value-h");
        cache.put("i", "value-i");
        cache.put("j", "value-j");
        cache.put("k", "value-k");
        cache.put("l", "value-l");

        System.out.println("\n--- Step 4: Observer invalidation ---");
        cache.put("product:1", "Laptop");
        node2.put("product:1", "OldLaptop");
        System.out.println("Node2 direct get product:1 = " + node2.get("product:1"));
        cache.put("product:1", "NewLaptop");
        System.out.println("Node2 direct get product:1 after invalidation = " + node2.get("product:1"));
        System.out.println("Distributed cache get product:1 = " + cache.get("product:1"));

        System.out.println("\n--- Step 5: TTL expiry ---");
        cache.put("session:abc", "token123", 1);
        System.out.println("Get session:abc before expiry = " + cache.get("session:abc"));
        Thread.sleep(2000);
        System.out.println("Get session:abc after expiry = " + cache.get("session:abc"));

        System.out.println("\n--- Step 6: Node added mid operation ---");
        ((DistributedCache) cache).addNode(new CacheNode("node-4", config, lru));
        cache.put("user:1", "Alice-Updated");
        System.out.println("Get user:1 with 4 nodes = " + cache.get("user:1"));
    }
}
