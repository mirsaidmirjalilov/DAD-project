package com.example.fooddeliverymarketplace.repository.authrepository;

import com.example.fooddeliverymarketplace.entity.auth.AuthRole;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRoleRepository extends JpaRepository<@NonNull AuthRole,@NonNull Long> {
    @Query(nativeQuery = true,
            value = "select ar.* from auth_roles ar join auth_user_roles aur on aur.role_id = ar.id where aur.user_id=:userId")
    List<AuthRole> findAuthRolesByUserId(@Param("userId") Long id);
}
