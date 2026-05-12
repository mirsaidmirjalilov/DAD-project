package com.example.fooddeliverymarketplace.service.serviceimpl;

import com.example.fooddeliverymarketplace.entity.User;
import com.example.fooddeliverymarketplace.exception.user.UserAlreadyExistException;
import com.example.fooddeliverymarketplace.exception.user.UserNotFoundException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    public UserResponse create(UserRequest userRequest, Authentication authentication) {
        checkIsAdmin(authentication);

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
            int size,
            Authentication authentication
    ) {
        checkIsAdmin(authentication);

        Pageable pageable = PageRequest.of(page, size);

        Specification<User> specification = specificationService.getUserSpecification(role, userStatus);

        Page<User> users = userRepository.findAll(specification, pageable);

        return users
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(@NonNull Long userId,Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        String email = authentication.getName();

        checkIsAdmin(authentication);

        checkIsOwner(user,email);

        return userMapper.toUserResponse(user);
    }



    @Override
    public UserResponse updateUser(@NonNull Long userId, UserRequest userRequest,Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        String email = authentication.getName();

        checkIsAdmin(authentication);

        checkIsOwner(user,email);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUserById(@NonNull Long userId,Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        checkIsAdmin(authentication);

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    public UserResponse changeUserStatus(
            @NonNull Long userId,
            UserStatus userStatus,
            Authentication authentication
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        checkIsAdmin(authentication);

        user.setStatus(userStatus);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
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

    private void checkIsAdmin(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("you are not admin");
        }
    }

    private void checkIsOwner(User user,String email) {
        boolean isOwner = user.getEmail().equals(email);

        if (!isOwner) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
