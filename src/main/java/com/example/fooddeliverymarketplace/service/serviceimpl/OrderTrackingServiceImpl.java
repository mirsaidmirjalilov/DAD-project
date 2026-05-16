package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.document.ordertracking.OrderTrackingDocument;
import com.example.fooddeliverymarketplace.document.ordertracking.OrderTrackingEvent;
import com.example.fooddeliverymarketplace.document.ordertracking.OrderTrackingRepository;
import com.example.fooddeliverymarketplace.service.OrderTrackingService;
import com.example.fooddeliverymarketplace.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderTrackingServiceImpl implements OrderTrackingService {

    private final OrderTrackingRepository orderTrackingRepository;

    @Override
    public OrderTrackingDocument addEvent(
            Long orderId,
            OrderStatus status,
            String message,
            Map<String, Object> metadata
    ) {
        OrderTrackingDocument trackingDocument = orderTrackingRepository
                .findByOrderId(orderId)
                .orElseGet(() -> OrderTrackingDocument.builder()
                        .orderId(orderId)
                        .build());

        OrderTrackingEvent event = OrderTrackingEvent.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .metadata(metadata)
                .build();

        trackingDocument.addEvent(event);

        return orderTrackingRepository.save(trackingDocument);
    }

    @Override
    public OrderTrackingDocument getTrackingByOrderId(Long orderId) {
        return orderTrackingRepository
                .findByOrderId(orderId)
                .orElseGet(() -> OrderTrackingDocument.builder()
                        .orderId(orderId)
                        .build());
    }
}