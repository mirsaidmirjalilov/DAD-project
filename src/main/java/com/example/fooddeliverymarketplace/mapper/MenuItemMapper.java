package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.MenuItem;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    public MenuItemResponse toMenuItemResponse(MenuItem foundInMenu) {
        return new MenuItemResponse(
                foundInMenu.getId(),
                foundInMenu.getName(),
                foundInMenu.getDescription(),
                foundInMenu.getPrice(),
                foundInMenu.getAvailable(),
                foundInMenu.getRestaurant().getRestaurantName()
        );
    }
}
