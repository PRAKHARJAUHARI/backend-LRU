
package com.example.lrucache.model;

public class CacheItem {
    private String key;
    private String value;

    public CacheItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
