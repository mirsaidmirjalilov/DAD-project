package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.ratelimiter.TokenBucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final TokenBucket orderRateLimiter = new TokenBucket(10,10);

    public boolean allowOrderCreation(String userId){
        boolean allowed = orderRateLimiter.tryConsume("order: " + userId);

        if (!allowed) {
            System.out.println("Rate limit reached for user: " + userId);
        }
        return allowed;
    }
}
