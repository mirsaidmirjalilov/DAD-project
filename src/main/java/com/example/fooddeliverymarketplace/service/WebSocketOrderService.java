package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.OrderStatusUpdate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WebSocketOrderService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketOrderService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendOrderUpdate(Long orderId, String status, String message) {
        OrderStatusUpdate update = new OrderStatusUpdate(
                orderId,
                status,
                message,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/orders/" + orderId, update);
    }
}