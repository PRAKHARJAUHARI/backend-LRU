package com.example.lrucache.controller;

import com.example.lrucache.model.CacheItem;
import com.example.lrucache.service.LRUCache;
import com.example.lrucache.service.LRUCacheWithTTL;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CacheController {

    private LRUCache lruCache;
    private LRUCacheWithTTL lruCacheWithTTL;

    @PostMapping("/lru/init")
    public Map<String, String> initLru(@RequestBody Map<String, Integer> request) {
        lruCache = new LRUCache(request.get("capacity"));
        Map<String, String> response = new HashMap<>();
        response.put("status", "LRU Cache initialized");
        return response;
    }

    @PostMapping("/lru/put")
    public Map<String, String> putLru(@RequestBody Map<String, String> request) {
        lruCache.put(request.get("key"), request.get("value"));
        Map<String, String> response = new HashMap<>();
        response.put("status", "Inserted into LRU Cache");
        return response;
    }

    @GetMapping("/lru/cache")
    public Map<String, Object> getLruCache() {
        Map<String, Object> response = new HashMap<>();
        if (lruCache == null) {
            response.put("error", "LRU Cache is not initialized.");
            return response;
        }
        response.put("cache", lruCache.getAll());
        return response;
    }

    @GetMapping("/lru/get/{key}")
    public Map<String, String> getLruKey(@PathVariable String key) {
        Map<String, String> response = new HashMap<>();

        if (lruCache == null) {
            response.put("error", "LRU Cache is not initialized.");
            return response;
        }

        String value = lruCache.get(key);
        System.out.print(value);

        if (value != null) {
            response.put("key", key);
            response.put("value", value);
        } else {
            response.put("error", "Key not found in LRU Cache.");
        }

        return response;
    }


    @PostMapping("/lru-ttl/init")
    public Map<String, String> initLruTtl(@RequestBody Map<String, Integer> request) {
        lruCacheWithTTL = new LRUCacheWithTTL(request.get("capacity"));
        Map<String, String> response = new HashMap<>();
        response.put("status", "LRU Cache with TTL initialized");
        return response;
    }

    @PostMapping("/lru-ttl/put")
    public Map<String, String> putLruTtl(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("key");
        String value = (String) request.get("value");
        int ttlInSeconds = (Integer) request.get("ttlInSeconds");
        lruCacheWithTTL.put(key, value, ttlInSeconds);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Inserted into LRU Cache with TTL");
        return response;
    }

    @GetMapping("/lru-ttl/cache")
    public Map<String, Object> getLruTtlCache() {
        Map<String, Object> response = new HashMap<>();
        if (lruCacheWithTTL == null) {
            response.put("error", "LRU Cache with TTL is not initialized.");
            return response;
        }
        response.put("cache", lruCacheWithTTL.getAll());
        return response;
    }

    @GetMapping("/lru-ttl/get/{key}")
    public Map<String, String> getLruTtlKey(@PathVariable String key) {
        Map<String, String> response = new HashMap<>();

        if (lruCacheWithTTL == null) {
            response.put("error", "LRU Cache with TTL is not initialized.");
            return response;
        }

        String value = lruCacheWithTTL.get(key);

        if (value != null) {
            response.put("key", key);
            response.put("value", value);
        } else {
            response.put("error", "Key not found or expired in LRU TTL Cache.");
        }
        return response;
    }
}
