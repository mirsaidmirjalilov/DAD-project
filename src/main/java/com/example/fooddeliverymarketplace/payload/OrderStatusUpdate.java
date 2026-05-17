package com.example.fooddeliverymarketplace.payload;

import java.time.LocalDateTime;

public record OrderStatusUpdate(
        Long orderId,
         String status,
         String message,
         LocalDateTime timestamp
) {
}
