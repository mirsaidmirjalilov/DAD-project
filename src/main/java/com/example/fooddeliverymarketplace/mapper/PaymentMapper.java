package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.Payment;
import com.example.fooddeliverymarketplace.payload.payment.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMethod()
        );
    }
}
