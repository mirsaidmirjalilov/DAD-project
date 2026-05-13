package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.delivery.DeliveryResponseWithOrder;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DeliveryService {
    List<DeliveryResponseWithOrder> getAvailableDeliveries();

    void acceptDelivery(Long deliveryId, Authentication authentication);

    void pickUpDelivery(Long deliveryId, Authentication authentication);

    void completeDelivery(Long deliveryId, Authentication authentication);

    void onTheWayDelivery(Long deliveryId, Authentication authentication);

    List<DeliveryResponseWithOrder> getUserDeliveries(Long orderId, Authentication authentication);
}
