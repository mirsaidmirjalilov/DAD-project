package com.example.fooddeliverymarketplace.repository.authrepository;

import com.example.fooddeliverymarketplace.entity.auth.AuthUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findUserByUsername(@NonNull String username);
}
