package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemRequest;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

public interface MenuItemService {
    MenuItemResponse getByMenuItemId(Long menuItemId);

    MenuItemResponse createMenuItem(@Valid MenuItemRequest menuItemRequest, Authentication authentication);

    MenuItemResponse update(Long menuItemId,@Valid MenuItemRequest menuItemRequest, Authentication authentication);

    void delete(Long menuItemId, Authentication authentication);
}
