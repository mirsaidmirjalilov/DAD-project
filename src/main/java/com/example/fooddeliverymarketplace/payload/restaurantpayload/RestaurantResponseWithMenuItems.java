package com.example.fooddeliverymarketplace.payload.restaurantpayload;

import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;

public record RestaurantResponseWithMenuItems(
        MenuItemResponse menuItemResponse,
        String RestaurantName
) {
}
