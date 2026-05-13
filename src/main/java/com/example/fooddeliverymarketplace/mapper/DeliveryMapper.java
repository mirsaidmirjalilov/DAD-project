package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.payload.delivery.DeliveryResponseWithOrder;
import com.example.fooddeliverymarketplace.payload.order.OrderResponse;
import com.example.fooddeliverymarketplace.utils.DeliveryStatus;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {
    public DeliveryResponseWithOrder toDeliveryResponseWithOrder(DeliveryStatus deliveryStatus, String address , OrderResponse orderResponse) {
        return new DeliveryResponseWithOrder(
                orderResponse,
                address,
                deliveryStatus
        );
    }
}
