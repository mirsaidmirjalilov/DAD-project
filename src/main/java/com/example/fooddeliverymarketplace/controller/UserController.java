package com.example.fooddeliverymarketplace.controller;

import com.example.fooddeliverymarketplace.entity.auth.AuthUser;
import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.userpayload.UserRequest;
import com.example.fooddeliverymarketplace.payload.userpayload.UserResponse;
import com.example.fooddeliverymarketplace.service.UserService;
import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> create(
            @RequestBody @Valid UserRequest userRequest,
            Authentication authentication
    ) {
        UserResponse response = userService.create(userRequest,authentication);
        return ResponseEntity.status(201).body(BaseResponse.ok(response));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> getUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        List<UserResponse> allUsers = userService.getAllUsers(role, userStatus, page, size,authentication);
        return ResponseEntity.status(200).body(BaseResponse.ok(allUsers));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAllRoles('ADMIN','OWNER')")
    public ResponseEntity<BaseResponse> getUser(
            @PathVariable @NonNull Long userId,
            Authentication authentication
    ) {
        UserResponse userById = userService.getUserById(userId,authentication);
        return ResponseEntity.status(200).body(BaseResponse.ok(userById));
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseEntity<BaseResponse> updateUser(
            @PathVariable @NonNull Long userId,
            @RequestBody @Valid UserRequest userRequest,
            Authentication authentication
    ) {
        UserResponse userResponse = userService.updateUser(userId, userRequest,authentication);
        return ResponseEntity.status(200).body(BaseResponse.ok(userResponse));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse> deleteUser(
            @PathVariable @NonNull Long userId,
            Authentication authentication
    ) {
        userService.deleteUserById(userId,authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> changeStatus(
            @PathVariable @NonNull Long userId,
            @RequestBody @Valid UserStatus userStatus,
            Authentication authentication
            ) {
        UserResponse userResponse = userService.changeUserStatus(userId, userStatus,authentication);
        return ResponseEntity.status(200).body(BaseResponse.ok(userResponse));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getCurrentUser(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        String email = authUser.getFullName();

        UserResponse byEmail = userService.getUserByEmail(email);

        return ResponseEntity.status(200).body(BaseResponse.ok(byEmail));
    }
}
