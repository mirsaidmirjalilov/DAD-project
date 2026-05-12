package com.example.fooddeliverymarketplace.payload.restaurantpayload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantRequest(
        @NotBlank String restaurantName,
        String description,
        @NotBlank String address,
        @NotBlank String phoneNumber
) {
}
