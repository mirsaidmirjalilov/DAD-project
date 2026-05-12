package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantRequest;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponse;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponseWithMenuItems;
import com.example.fooddeliverymarketplace.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService  restaurantService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getRestaurants(
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) Float rating,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<RestaurantResponse> all = restaurantService.findAllByCriterias(restaurantName,rating,active,page,size);

        return ResponseEntity.status(200).body(BaseResponse.ok(all));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<BaseResponse> getRestaurant(@PathVariable("restaurantId") Long restaurantId){
        RestaurantResponse response = restaurantService.findById(restaurantId);

        return ResponseEntity.status(200).body(BaseResponse.ok(response));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> createRestaurant(
            @RequestBody @Valid RestaurantRequest restaurantRequest,
            Authentication authentication
    ){
        RestaurantResponse response = restaurantService.create(restaurantRequest, authentication);
        return ResponseEntity.status(201).body(BaseResponse.ok(response));
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid RestaurantRequest restaurantRequest,
            Authentication authentication
    ){
        RestaurantResponse updated = restaurantService.update(restaurantId, restaurantRequest, authentication);

        return ResponseEntity.status(200).body(BaseResponse.ok(updated));
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> deleteRestaurant(
            @PathVariable Long restaurantId,
            Authentication authentication
    ){
        restaurantService.delete(restaurantId,authentication);

        return ResponseEntity.status(204).build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getMyRestaurants(
            Authentication authentication
    ){
        List<RestaurantResponse> allByOwner = restaurantService.findAllByOwner(authentication);

        return ResponseEntity.status(200).body(BaseResponse.ok(allByOwner));
    }

    @GetMapping("/{restaurantId}/menu-items")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getMenuItems(@PathVariable Long restaurantId){
        List<RestaurantResponseWithMenuItems> allItemsByRestaurantId = restaurantService.getAllItemsByRestaurantId(restaurantId);

        return ResponseEntity.status(200).body(BaseResponse.ok(allItemsByRestaurantId));
    }
}
