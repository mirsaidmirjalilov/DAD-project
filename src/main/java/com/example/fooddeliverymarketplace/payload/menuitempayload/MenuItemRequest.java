package com.example.fooddeliverymarketplace.payload.menuitempayload;

import jakarta.validation.constraints.NotBlank;

public record MenuItemRequest(
        @NotBlank String name,
        String description,
        @NotBlank Integer price,
        @NotBlank Long restaurantId
) {
}
