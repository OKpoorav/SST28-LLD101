package com.example.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Long> counters = new HashMap<>();

    // Static holder — lazy, thread-safe, loaded by class-loader guarantee
    private static class Holder {
        private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    }

    // Private constructor — blocks direct instantiation
    // Also blocks reflection: if the singleton already exists, throw
    private MetricsRegistry() {
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Use getInstance() — reflection not allowed");
        }
    }

    public static MetricsRegistry getInstance() {
        return Holder.INSTANCE;
    }

    // Preserve singleton across serialization/deserialization
    @Serial
    private Object readResolve() {
        return getInstance();
    }

    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }
}
