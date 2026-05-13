package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.payment.PaymentRequest;
import com.example.fooddeliverymarketplace.payload.payment.PaymentResponse;
import jakarta.validation.Valid;

public interface PaymentService {
    PaymentResponse create(@Valid PaymentRequest paymentRequest);

    PaymentResponse getPaymentByOrder(Long orderId);
}
