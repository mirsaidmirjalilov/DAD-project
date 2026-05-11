package com.example.fooddeliverymarketplace.payload.authpayload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record RegisterRequest(
        @NotBlank(message = "full name is required")
        String fullName,

        @Email(message = "invalid email")
        @NotBlank(message = "email is required")
        String email,

        @NotBlank(message = "phone number is required")
        @Pattern(regexp = "^[+]?[0-9]{9,15}$",
                message = "Invalid phone number"
        )
        String phoneNumber,

        @NotBlank(message = "password is required")
        String password,

        List<Long> rolesIDs
) {
}
