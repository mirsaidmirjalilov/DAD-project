package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.ErrorDTO;
import com.example.fooddeliverymarketplace.payload.order.OrderRequest;
import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.example.fooddeliverymarketplace.payload.order.UpdateOrderStatusRequest;
import com.example.fooddeliverymarketplace.service.OrderService;
import com.example.fooddeliverymarketplace.service.RateLimiterService;
import com.example.fooddeliverymarketplace.service.userdetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final RateLimiterService rateLimiterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> createOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            Authentication authentication
    ) {
        OrderResponse orderResponse = orderService.create(orderRequest, authentication);

        String userId = getUserIdFromAuthentication(authentication);

        if (!rateLimiterService.allowOrderCreation(userId)) {
            return ResponseEntity.status(429)
                    .body(BaseResponse.error(new ErrorDTO(
                                            "Too Many Requests",
                                            "api/v1/orders",
                                            429,
                                            LocalDateTime.now()
                                    )
                            )
                    );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(orderResponse));
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getMyOrders(Authentication authentication) {
        List<OrderResponse> myOrders = orderService.getMyOrders(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(myOrders));
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getOrderById(
            @PathVariable("orderId") Long orderId,
            Authentication authentication
    ) {
        OrderResponse orderById = orderService.getOrderById(orderId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(orderById));
    }

    @PutMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> updateOrderStatus(
            @PathVariable Long orderId,
            Authentication authentication,
            @RequestBody UpdateOrderStatusRequest orderStatus
    ) {
        orderService.updateOrderStatus(orderId, orderStatus, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.ok());
    }

    @PutMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        orderService.cancelOrder(orderId, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.ok());
    }

    private String getUserIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.authUser().getId().toString();
        }

        return authentication.getName();
    }
}
