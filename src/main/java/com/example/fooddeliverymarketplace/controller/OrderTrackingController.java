package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.document.OrderTrackingDocument;
import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.service.OrderTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderTrackingController {
    private final OrderTrackingService orderTrackingService;

    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<BaseResponse<OrderTrackingDocument>> getOrderTracking(
            @PathVariable Long orderId
    ) {
        OrderTrackingDocument tracking = orderTrackingService.getTrackingByOrderId(orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.ok(tracking));
    }
}
