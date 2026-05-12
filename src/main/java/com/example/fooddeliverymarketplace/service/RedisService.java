package com.example.fooddeliverymarketplace.service;

import java.time.Duration;

public interface RedisService {
    void put(String key, Object value, Duration ttl);
    <T> T get(String key, Class<T> clazz);
    void delete(String key);
}
