package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemRequest;
import com.example.fooddeliverymarketplace.payload.menuitempayload.MenuItemResponse;
import com.example.fooddeliverymarketplace.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu-items")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @GetMapping("/{menuItemId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<BaseResponse> getMenuItem(
            @PathVariable("menuItemId") Long menuItemId
    ) {
        MenuItemResponse byMenuItemId = menuItemService.getByMenuItemId(menuItemId);

        return ResponseEntity.status(HttpStatus.FOUND).body(BaseResponse.ok(byMenuItemId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> createMenuItem(
            @RequestBody @Valid MenuItemRequest menuItemRequest,
            Authentication authentication
    ){
        MenuItemResponse menuItem = menuItemService.createMenuItem(menuItemRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(menuItem));
    }

    @PutMapping("/{menuItemId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> updateMenuItem(
            @PathVariable Long menuItemId,
            @RequestBody @Valid MenuItemRequest menuItemRequest,
            Authentication authentication
    ){
        MenuItemResponse update = menuItemService.update(menuItemId, menuItemRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(update));
    }

    @DeleteMapping("/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> deleteMenuItem(
            @PathVariable Long menuItemId,
            Authentication authentication
    ){
        menuItemService.delete(menuItemId,authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.ok());
    }
}
