package com.example.fooddeliverymarketplace.repository.order;

import com.example.fooddeliverymarketplace.entity.order.Order;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);

    Optional<Order> findById(@NotBlank Long orderId);
}
