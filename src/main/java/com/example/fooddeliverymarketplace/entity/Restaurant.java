package com.example.fooddeliverymarketplace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_name",nullable = false)
    private String restaurantName;

    @Column(name = "description")
    private String description;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "phone_number",nullable = false)
    @Pattern(regexp = "^[+]?[0-9]{9,15}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (active == null) {
            active = true;
        }

        if (rating == null) {
            rating = 0.0F;
        }
    }
}
