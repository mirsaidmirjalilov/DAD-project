package com.example.fooddeliverymarketplace.payload.order;

public record OrderRequest(
        String address,
        String comment
) {
}
