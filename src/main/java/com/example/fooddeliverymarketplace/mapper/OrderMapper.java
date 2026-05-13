package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.order.Order;
import com.example.fooddeliverymarketplace.payload.order.OrderItemResponse;
import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order, List<OrderItemResponse> orderItems) {
        return new OrderResponse(
                order.getId(),
                order.getRestaurant().getRestaurantName(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt(),
                orderItems
        );
    }
}
