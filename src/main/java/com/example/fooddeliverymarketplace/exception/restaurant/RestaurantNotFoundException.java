package com.example.fooddeliverymarketplace.exception.restaurant;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
