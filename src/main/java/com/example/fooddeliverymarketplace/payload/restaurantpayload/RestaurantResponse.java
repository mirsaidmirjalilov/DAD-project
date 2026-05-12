package com.example.fooddeliverymarketplace.payload.restaurantpayload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantResponse (
        Long id,
        String name,
        String description,
        String address,
        String phoneNumber,
        Float rating,
        Boolean active,
        String ownerName
) implements Serializable {
}
