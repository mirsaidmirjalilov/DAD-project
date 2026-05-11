package com.example.fooddeliverymarketplace.mapper;

import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.payload.userpayload.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(final User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
