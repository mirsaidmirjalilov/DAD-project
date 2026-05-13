package com.example.fooddeliverymarketplace.payload.delivery;

import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.example.fooddeliverymarketplace.utils.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeliveryResponseWithOrder(
        OrderResponse orderResponse,
        String deliveryAddress,
        DeliveryStatus deliveryStatus
) {
}
