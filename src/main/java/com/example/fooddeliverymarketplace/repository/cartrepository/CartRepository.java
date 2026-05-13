package com.example.fooddeliverymarketplace.repository.cartrepository;

import com.example.fooddeliverymarketplace.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long id);
}
