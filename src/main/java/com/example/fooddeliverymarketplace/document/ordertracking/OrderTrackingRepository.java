package com.example.fooddeliverymarketplace.document.ordertracking;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderTrackingRepository extends MongoRepository<OrderTrackingDocument, String> {

    Optional<OrderTrackingDocument> findByOrderId(Long orderId);
}