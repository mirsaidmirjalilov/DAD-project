package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.document.OrderTrackingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderTrackingRepository extends MongoRepository<OrderTrackingDocument,String> {
    Optional<OrderTrackingDocument> findByOrderId(Long orderId);
}
