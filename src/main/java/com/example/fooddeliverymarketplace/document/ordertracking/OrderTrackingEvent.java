package com.example.fooddeliverymarketplace.document.ordertracking;

import com.example.fooddeliverymarketplace.utils.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingEvent {

    private OrderStatus status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, Object> metadata;
}