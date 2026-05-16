package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.document.ordertracking.OrderTrackingDocument;
import com.example.fooddeliverymarketplace.utils.OrderStatus;

import java.util.Map;

public interface OrderTrackingService {

    OrderTrackingDocument addEvent(
            Long orderId,
            OrderStatus status,
            String message,
            Map<String, Object> metadata
    );

    OrderTrackingDocument getTrackingByOrderId(Long orderId);
}