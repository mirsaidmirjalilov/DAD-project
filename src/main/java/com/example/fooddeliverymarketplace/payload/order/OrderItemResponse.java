package com.example.fooddeliverymarketplace.payload.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long menuItemId,
        String menuItemName,
        Integer quantity,
        BigDecimal price
) {
}