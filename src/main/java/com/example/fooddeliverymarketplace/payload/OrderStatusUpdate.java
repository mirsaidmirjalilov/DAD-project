package com.example.fooddeliverymarketplace.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdate {
    private Long orderId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}