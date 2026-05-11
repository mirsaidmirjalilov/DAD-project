package com.example.fooddeliverymarketplace.repository;

import com.example.fooddeliverymarketplace.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository< @NonNull User,@NonNull Long> {
    Optional<User> findByEmail(@Email(message = "invalid email") @NotBlank(message = "email is required") String email);

    boolean existsByEmail(@Email(message = "invalid email") @NotBlank(message = "email is required") String email);

    Page<@NonNull User> findAll(Specification<@NonNull User> specification, Pageable pageable);
}
