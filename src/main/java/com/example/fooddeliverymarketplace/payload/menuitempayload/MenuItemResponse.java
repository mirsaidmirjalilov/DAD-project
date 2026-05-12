package com.example.fooddeliverymarketplace.payload.menuitempayload;

public record MenuItemResponse(
         Long id,
         String name,
         String description,
         Integer price,
         Boolean available,
         String restaurantName
) {
}
