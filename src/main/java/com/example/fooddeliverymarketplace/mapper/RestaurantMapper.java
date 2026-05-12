package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.Restaurant;
import com.example.fooddeliverymarketplace.payload.restaurantpayload.RestaurantResponse;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    public RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getRestaurantName(),
                restaurant.getDescription(),
                restaurant.getAddress(),
                restaurant.getPhoneNumber(),
                restaurant.getRating(),
                restaurant.getActive(),
                restaurant.getOwner().getFullName()
        );
    }
}
