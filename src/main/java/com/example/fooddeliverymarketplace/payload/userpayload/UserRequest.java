package com.example.fooddeliverymarketplace.payload.userpayload;

import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserRequest(
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

        UserStatus status,

        Role role
) {
}
