package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.document.OrderTrackingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderTrackingRepository extends MongoRepository<OrderTrackingDocument,String> {
    Optional<OrderTrackingDocument> findByOrderId(Long orderId);
}
