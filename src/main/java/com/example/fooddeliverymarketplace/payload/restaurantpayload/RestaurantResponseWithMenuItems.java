package com.example.fooddeliverymarketplace.payload.restaurantpayload;

import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;

import java.io.Serializable;

public record RestaurantResponseWithMenuItems(
        MenuItemResponse menuItemResponse,
        String RestaurantName
) implements Serializable {
}
