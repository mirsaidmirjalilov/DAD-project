package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.Payment;
import com.example.fooddeliverymarketplace.entity.order.Order;
import com.example.fooddeliverymarketplace.exception.OrderNotFoundException;
import com.example.fooddeliverymarketplace.exception.PaymentNotFoundException;
import com.example.fooddeliverymarketplace.mapper.PaymentMapper;
import com.example.fooddeliverymarketplace.payload.payment.PaymentRequest;
import com.example.fooddeliverymarketplace.payload.payment.PaymentResponse;
import com.example.fooddeliverymarketplace.repository.PaymentRepository;
import com.example.fooddeliverymarketplace.repository.order.OrderRepository;
import com.example.fooddeliverymarketplace.service.PaymentService;
import com.example.fooddeliverymarketplace.utils.OrderStatus;
import com.example.fooddeliverymarketplace.utils.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest paymentRequest) {
        Order order = orderRepository.findById(paymentRequest.orderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        boolean paymentExists = paymentRepository.existsByOrderId(order.getId());

        if (paymentExists) {
            throw new IllegalStateException(
                    "Payment already exists for this order"
            );
        }

        Payment payment = Payment.builder()
                .order(order)
                .method(paymentRequest.method())
                .amount(order.getTotalPrice())
                .status(PaymentStatus.PAID)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        order.setStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);
        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    @Cacheable(value = "payments", key = "#orderId")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public PaymentResponse getPaymentByOrder(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return paymentMapper.toPaymentResponse(payment);
    }

    @CacheEvict(cacheNames = {"payments"}, allEntries = true)
    @Scheduled(cron = "0 0 * * * *")
    public void evictCache() {
        log.info("restaurant related cache evict");
    }
}
