package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.service.WebSocketOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class WebSocketTestController {

    private final WebSocketOrderService webSocketOrderService;

    public WebSocketTestController(WebSocketOrderService webSocketOrderService) {
        this.webSocketOrderService = webSocketOrderService;
    }

    @PostMapping("/order-update/{orderId}")
    public ResponseEntity<String> sendTestUpdate(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(defaultValue = "Status updated") String message) {
        
        webSocketOrderService.sendOrderUpdate(orderId, status, message);
        return ResponseEntity.ok("✅ WebSocket notification sent for order " + orderId);
    }
}