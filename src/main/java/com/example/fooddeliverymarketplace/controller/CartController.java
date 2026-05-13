package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.cart.CartRequest;
import com.example.fooddeliverymarketplace.payload.cart.CartResponse;
import com.example.fooddeliverymarketplace.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse> mapCartItem(
            @RequestBody @Valid CartRequest cartRequest,
            Authentication authentication
    ) {
        CartResponse cartResponse = cartService.create(cartRequest, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(cartResponse));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getCartItems(Authentication authentication) {
        CartResponse userCart = cartService.getUserCart(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok(userCart));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> deleteCartItems(Authentication authentication) {
        cartService.clearCart(authentication);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.ok());
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<BaseResponse> deleteCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication,
            Integer quantity
    ){
        cartService.deleteItemByCartId(cartItemId,authentication,quantity);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.ok());
    }

    @PutMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> updateCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication,
            Integer quantity
    ){
        cartService.addQuantityToCartItem(authentication,cartItemId,quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok());
    }
}
