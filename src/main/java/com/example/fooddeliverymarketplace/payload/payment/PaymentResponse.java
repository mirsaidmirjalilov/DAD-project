package com.example.fooddeliverymarketplace.payload.payment;

import com.example.fooddeliverymarketplace.utils.PaymentMethod;
import com.example.fooddeliverymarketplace.utils.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        PaymentMethod method
) {
}
