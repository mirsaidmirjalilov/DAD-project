package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.entity.Delivery;
import com.example.fooddeliverymarketplace.utils.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery> findDeliveriesByStatus(DeliveryStatus status);

    Optional<Delivery> findByIdWithLock(Long deliveryId);

    List<Delivery> findDeliveriesByOrderId(Long orderId);
}
