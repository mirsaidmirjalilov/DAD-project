package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.cart.Cart;
import com.example.fooddeliverymarketplace.entity.cart.CartItem;
import com.example.fooddeliverymarketplace.payload.cart.CartResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {
    public CartResponse toCartResponse(Cart cart, String RestaurantName, List<CartItem> cartItems) {
        return new CartResponse(
                RestaurantName,
                cart.getTotalPrice(),
                cartItems
        );
    }
}
