package com.example.fooddeliverymarketplace.payload.cart;

import com.example.fooddeliverymarketplace.entity.cart.CartItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CartResponse(
        String restaurantName,
        java.math.@jakarta.validation.constraints.Positive BigDecimal totalPrice,
        List<CartItem> cartItems
) {
}
