package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.delivery.DeliveryResponseWithOrder;
import com.example.fooddeliverymarketplace.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping("/available")
    @PreAuthorize("hasRole('COURIER')")
    public ResponseEntity<BaseResponse> getAvailableDeliveries() {
        List<DeliveryResponseWithOrder> availableDeliveries = deliveryService.getAvailableDeliveries();

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(availableDeliveries));
    }

    @PutMapping("/{deliveryId}/accept")
    @PreAuthorize("hasRole('COURIER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> acceptDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            Authentication authentication
    ) {
        deliveryService.acceptDelivery(deliveryId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok());
    }

    @PutMapping("/{deliveryId}/pickup")
    @PreAuthorize("hasRole('COURIER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> pickupDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            Authentication authentication
    ) {
        deliveryService.pickUpDelivery(deliveryId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok());
    }

    @PutMapping("/{deliveryId}/complete")
    @PreAuthorize("hasRole('COURIER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> completeDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            Authentication authentication
    ) {
        deliveryService.completeDelivery(deliveryId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok());
    }

    @PutMapping("/{deliveryId}/on_the_way")
    @PreAuthorize("hasRole('COURIER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> onTheWayDelivery(
            @PathVariable("deliveryId") Long deliveryId,
            Authentication authentication
    ) {
        deliveryService.onTheWayDelivery(deliveryId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok());
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> orderDelivery(
            @PathVariable Long orderId,
            Authentication authentication
    ){
        List<DeliveryResponseWithOrder> userDeliveries = deliveryService.getUserDeliveries(orderId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(userDeliveries));
    }
}
