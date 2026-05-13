package com.example.fooddeliverymarketplace.repository.cartrepository;

import com.example.fooddeliverymarketplace.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByCartId(Long id);

    List<CartItem> getAllByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndMenuItemId(Long id, Long id1);

    void deleteAllByCartId(Long id);
}
