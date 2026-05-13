package com.example.fooddeliverymarketplace.payload.payment;

import com.example.fooddeliverymarketplace.utils.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentRequest(
        @NotBlank Long orderId,

        @NotNull(message = "Payment method must be specified")
        PaymentMethod method,

        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal amount
) {
}
