package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.cart.CartRequest;
import com.example.fooddeliverymarketplace.payload.cart.CartResponse;
import org.springframework.security.core.Authentication;

public interface CartService {
    CartResponse create(CartRequest cartRequest, Authentication authentication);

    CartResponse getUserCart(Authentication authentication);

    void clearCart(Authentication authentication);

    void deleteItemByCartId(Long cartItemId, Authentication authentication, Integer quantity);

    CartResponse addQuantityToCartItem(Authentication authentication, Long cartItemId, Integer quantity);
}
