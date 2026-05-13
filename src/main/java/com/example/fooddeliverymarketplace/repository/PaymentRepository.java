package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.entity.Payment;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(@NotBlank Long aLong);

    boolean existsByOrderId(Long id);
}
