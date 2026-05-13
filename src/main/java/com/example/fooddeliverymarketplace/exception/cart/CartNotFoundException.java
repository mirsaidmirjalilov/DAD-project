package com.example.fooddeliverymarketplace.exception.cart;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
