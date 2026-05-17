package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.document.OrderTrackingDocument;
import com.example.fooddeliverymarketplace.document.OrderTrackingEvent;
import com.example.fooddeliverymarketplace.repository.OrderTrackingRepository;
import com.example.fooddeliverymarketplace.service.OrderTrackingService;
import com.example.fooddeliverymarketplace.utils.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
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
    @Cacheable(value = "trackingOrder",key = "#orderId")
    public OrderTrackingDocument getTrackingByOrderId(Long orderId) {
        return orderTrackingRepository
                .findByOrderId(orderId)
                .orElseGet(() -> OrderTrackingDocument.builder()
                        .orderId(orderId)
                        .build());
    }

    @Scheduled(cron = "0 0 * * * *")
    @CacheEvict(cacheNames = {"trackingOrder"},allEntries = true)
    public void evictCacheEvict() {
        log.info("tracking order related cache evict");
    }
}
