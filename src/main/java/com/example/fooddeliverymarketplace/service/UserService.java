package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.userpayload.UserRequest;
import com.example.fooddeliverymarketplace.payload.userpayload.UserResponse;
import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    UserResponse create(@Valid @NotNull UserRequest userRequest, Authentication authentication);

    List<UserResponse> getAllUsers(Role role, UserStatus userStatus, int page, int size,Authentication authentication);

    UserResponse getUserById(@NonNull Long userId,Authentication authentication);

    UserResponse updateUser(@NonNull Long userId, UserRequest userRequest,Authentication authentication);

    void deleteUserById(@NonNull Long userId,Authentication authentication);

    UserResponse changeUserStatus(@NonNull Long userId, @Valid UserStatus userStatus,Authentication authentication);

    UserResponse getUserByEmail(String email);
}
