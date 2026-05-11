package com.example.fooddeliverymarketplace.payload.userpayload;

import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        Long id,
        String fullName,
        String phoneNumber,
        String email,
        Role role,
        UserStatus status,
        LocalDateTime createdAt
) {
}
