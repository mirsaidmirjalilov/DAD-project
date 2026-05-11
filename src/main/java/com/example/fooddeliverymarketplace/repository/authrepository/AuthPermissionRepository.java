package com.example.fooddeliverymarketplace.repository.authrepository;

import com.example.fooddeliverymarketplace.entity.auth.AuthPermission;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthPermissionRepository extends JpaRepository<@NonNull AuthPermission,@NonNull Long> {
    @Query(nativeQuery = true,
            value = "select ap.* from auth_permissions ap join auth_role_permissions arp on ap.id=arp.permission_id where arp.role_id=:roleId;")
    List<AuthPermission> findAuthPermissionsByRoleId(@Param("roleId") Integer roleId);
}
