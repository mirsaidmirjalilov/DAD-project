package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.authpayload.LoginRequest;
import com.example.fooddeliverymarketplace.payload.authpayload.RegisterRequest;
import com.example.fooddeliverymarketplace.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {

        String token = authenticationService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(token));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(
            @RequestBody RegisterRequest registerRequest
    ){
        String register = authenticationService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(register));
    }
}