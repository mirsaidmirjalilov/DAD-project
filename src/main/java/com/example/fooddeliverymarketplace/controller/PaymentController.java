package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.payment.PaymentRequest;
import com.example.fooddeliverymarketplace.payload.payment.PaymentResponse;
import com.example.fooddeliverymarketplace.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> createPayment(
            @RequestBody @Valid PaymentRequest paymentRequest
    ) {
        PaymentResponse paymentResponse = paymentService.create(paymentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(paymentResponse));
    }

    @GetMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getPayment(
            @PathVariable Long orderId
    ){
        PaymentResponse paymentByOrder = paymentService.getPaymentByOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(paymentByOrder));
    }
}
