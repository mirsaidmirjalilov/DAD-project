package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.order.OrderItem;
import com.example.fooddeliverymarketplace.payload.order.OrderItemResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemMapper {
    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getMenuItem().getId(),
                orderItem.getMenuItem().getName(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}
