package com.example.fooddeliverymarketplace.entity;

import com.example.fooddeliverymarketplace.entity.order.Order;
import com.example.fooddeliverymarketplace.utils.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime estimatedDeliveryTime;

    private LocalDateTime deliveredAt;
}
