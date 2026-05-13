package com.example.fooddeliverymarketplace.payload.order;

import com.example.fooddeliverymarketplace.utils.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String restaurantName,
        BigDecimal totalPrice,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
