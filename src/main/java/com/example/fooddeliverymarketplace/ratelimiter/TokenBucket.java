package com.example.fooddeliverymarketplace.ratelimiter;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class TokenBucket {
    private final long capacity;
    private final long refillRate;
    private final ConcurrentHashMap<String, BucketState> buckets = new ConcurrentHashMap<>();

    private static final class BucketState {
        long tokens;
        long lastRefillTime;
        BucketState(long tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }

    public boolean tryConsume(String key){
        long now = System.currentTimeMillis();
        BucketState state = buckets.computeIfAbsent(key, k -> new BucketState(capacity, now));

        synchronized (state) {
            long elapsed = now - state.lastRefillTime;
            long newTokens = elapsed * refillRate / 1000;
            if (newTokens > 0){
                state.tokens = Math.min(capacity, state.tokens + newTokens);
                state.lastRefillTime = now;
            }

            if (state.tokens >= 1){
                state.tokens--;
                return true;
            }
            return false;
        }
    }

    public void reset(String key){
        buckets.remove(key);
    }
}
