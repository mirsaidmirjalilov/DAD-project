package com.example.fooddeliverymarketplace.service.auth;

import com.example.fooddeliverymarketplace.entity.auth.AuthUser;
import com.example.fooddeliverymarketplace.payload.authpayload.LoginRequest;
import com.example.fooddeliverymarketplace.payload.authpayload.RegisterRequest;
import com.example.fooddeliverymarketplace.repository.authrepository.AuthRoleRepository;
import com.example.fooddeliverymarketplace.repository.authrepository.AuthUserRepository;
import com.example.fooddeliverymarketplace.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthRoleRepository authRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public String login(LoginRequest loginRequest) {
        String username = loginRequest.email();
        String password = loginRequest.password();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(username);
    }

    public String register(RegisterRequest registerRequest) {
        AuthUser authUser = AuthUser.builder()
                .fullName(registerRequest.fullName())
                .username(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(authRoleRepository.findAllById(registerRequest.rolesIDs()))
                .build();
        authUserRepository.save(authUser);

        return "You are registered";
    }
}

