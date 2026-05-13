package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.order.OrderRequest;
import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.example.fooddeliverymarketplace.payload.order.UpdateOrderStatusRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest orderRequest, Authentication authentication);

    List<OrderResponse> getMyOrders(Authentication authentication);

    OrderResponse getOrderById(Long orderId, Authentication authentication);

    void updateOrderStatus(Long orderId, UpdateOrderStatusRequest orderStatus, Authentication authentication);

    void cancelOrder(Long orderId, Authentication authentication);
}
