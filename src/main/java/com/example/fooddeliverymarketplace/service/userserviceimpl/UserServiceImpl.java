package com.example.fooddeliverymarketplace.service.userserviceimpl;

import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.exception.UserAlreadyExistException;
import com.example.fooddeliverymarketplace.exception.UserNotFoundException;
import com.example.fooddeliverymarketplace.mapper.UserMapper;
import com.example.fooddeliverymarketplace.payload.userpayload.UserRequest;
import com.example.fooddeliverymarketplace.payload.userpayload.UserResponse;
import com.example.fooddeliverymarketplace.repository.UserRepository;
import com.example.fooddeliverymarketplace.service.UserService;
import com.example.fooddeliverymarketplace.service.paging.SpecificationService;
import com.example.fooddeliverymarketplace.utils.Role;
import com.example.fooddeliverymarketplace.utils.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SpecificationService specificationService;

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public UserResponse create(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistException("User already exists");
        }

        User user = User.builder()
                .fullName(userRequest.fullName())
                .email(userRequest.email())
                .phoneNumber(userRequest.phoneNumber())
                .password(passwordEncoder.encode(userRequest.password()))
                .status(userRequest.status())
                .role(userRequest.role())
                .build();

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<UserResponse> getAllUsers(
            Role role,
            UserStatus userStatus,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<User> specification = specificationService.getUserSpecification(role, userStatus);

        Page<User> users = userRepository.findAll(specification, pageable);

        return users
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(@NonNull Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            return userMapper.toUserResponse(optionalUser.get());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserResponse updateUser(@NonNull Long userId, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setFullName(userRequest.fullName());
            user.setEmail(userRequest.email());
            user.setPhoneNumber(userRequest.phoneNumber());
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            user.setRole(userRequest.role());
            user.setStatus(userRequest.status());
            userRepository.save(user);

            return userMapper.toUserResponse(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void deleteUserById(@NonNull Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            optionalUser.get().setStatus(UserStatus.DELETED);
            userRepository.save(optionalUser.get());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserResponse changeUserStatus(@NonNull Long userId, UserStatus userStatus) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(userStatus);
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return userMapper.toUserResponse(optionalUser.get());
        }else  {
            throw new UserNotFoundException("User not found");
        }
    }
}
