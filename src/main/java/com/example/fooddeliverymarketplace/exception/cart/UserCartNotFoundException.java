package com.example.fooddeliverymarketplace.exception.cart;

public class UserCartNotFoundException extends RuntimeException {
    public UserCartNotFoundException(String message) {
        super(message);
    }
}
