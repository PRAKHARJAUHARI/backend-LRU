package com.example.lrucache.service;

import com.example.lrucache.model.CacheItem;
import java.util.*;

public class LRUCacheWithTTL {

    private class Node {
        String key, value;
        long expiryTime; // in milliseconds
        Node prev, next;

        Node(String key, String value, long ttlInSeconds) {
            this.key = key;
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlInSeconds * 1000;
        }
    }

    private int capacity;
    private Map<String, Node> map;
    private Node head, tail;

    public LRUCacheWithTTL(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    public String get(String key) {
        removeExpired();
        if (!map.containsKey(key)) {
            return null;
        }
        Node node = map.get(key);
        if (node.expiryTime < System.currentTimeMillis()) {
            map.remove(key);
            removeNode(node);
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    public void put(String key, String value, long ttlInSeconds) {
        removeExpired();
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            node.expiryTime = System.currentTimeMillis() + ttlInSeconds * 1000;
            moveToHead(node);
        } else {
            Node node = new Node(key, value, ttlInSeconds);
            if (map.size() == capacity) {
                map.remove(tail.key);
                removeNode(tail);
            }
            addNodeAtHead(node);
            map.put(key, node);
        }
    }

    public List<CacheItem> getAll() {
        removeExpired();
        List<CacheItem> items = new ArrayList<>();
        Node current = head;
        while (current != null) {
            items.add(new CacheItem(current.key, current.value));
            current = current.next;
        }
        return items;
    }

    private void removeExpired() {
        Node current = head;
        while (current != null) {
            if (current.expiryTime < System.currentTimeMillis()) {
                Node toRemove = current;
                current = current.next;
                map.remove(toRemove.key);
                removeNode(toRemove);
            } else {
                current = current.next;
            }
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNodeAtHead(node);
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void addNodeAtHead(Node node) {
        node.prev = null;
        node.next = head;
        if (head != null) {
            head.prev = node;
        }
        head = node;
        if (tail == null) {
            tail = head;
        }
    }
}
