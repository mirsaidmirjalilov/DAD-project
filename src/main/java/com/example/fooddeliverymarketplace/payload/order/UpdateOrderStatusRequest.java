package com.example.fooddeliverymarketplace.payload.order;

import com.example.fooddeliverymarketplace.utils.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus orderStatus
) {
}
