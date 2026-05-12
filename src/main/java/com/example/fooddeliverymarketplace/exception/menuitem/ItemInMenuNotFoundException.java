package com.example.fooddeliverymarketplace.exception.menuitem;

public class ItemInMenuNotFoundException extends RuntimeException {
    public ItemInMenuNotFoundException(String message) {
        super(message);
    }
}
