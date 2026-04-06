package com.distributedcache.db;

import java.util.HashMap;
import java.util.Map;

public class DatabaseLoader {

    private Map<String, Object> fakeDatabase;

    public DatabaseLoader() {
        fakeDatabase = new HashMap<>();
        fakeDatabase.put("user:1", "Alice");
        fakeDatabase.put("user:2", "Bob");
        fakeDatabase.put("user:3", "Charlie");
        fakeDatabase.put("product:1", "Laptop");
        fakeDatabase.put("product:2", "Phone");
    }

    public Object load(String key) {
        System.out.println("DB: loading key [" + key + "] from database.");
        return fakeDatabase.get(key);
    }
}
