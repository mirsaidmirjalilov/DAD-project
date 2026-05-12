package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantRequest;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponse;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponseWithMenuItems;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RestaurantService {
    RestaurantResponse findById(Long restaurantId);

    RestaurantResponse create(@Valid RestaurantRequest restaurantRequest, Authentication authentication);

    RestaurantResponse update(Long restaurantId, @Valid RestaurantRequest restaurantRequest, Authentication authentication);

    void delete(Long restaurantId, Authentication authentication);

    List<RestaurantResponse> findAllByOwner(Authentication authentication);

    List<RestaurantResponseWithMenuItems> getAllItemsByRestaurantId(Long restaurantId);

    List<RestaurantResponse> findAllByCriterias(String restaurantName, Float rating, Boolean active, int page, int size);
}
