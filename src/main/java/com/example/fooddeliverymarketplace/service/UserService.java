package com.example.fooddeliverymarketplace.service;

import com.example.fooddeliverymarketplace.payload.userpayload.UserRequest;
import com.example.fooddeliverymarketplace.payload.userpayload.UserResponse;
import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.util.List;

public interface UserService {
    UserResponse create(@Valid @NotNull UserRequest userRequest);

    List<UserResponse> getAllUsers(Role role, UserStatus userStatus, int page, int size);

    UserResponse getUserById(@NonNull Long userId);

    UserResponse updateUser(@NonNull Long userId, UserRequest userRequest);

    void deleteUserById(@NonNull Long userId);

    UserResponse changeUserStatus(@NonNull Long userId, @Valid UserStatus userStatus);

    UserResponse getUserByEmail(String email);
}
