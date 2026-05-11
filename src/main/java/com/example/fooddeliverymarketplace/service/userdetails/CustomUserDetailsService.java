package com.example.fooddeliverymarketplace.service.userdetails;

import com.example.fooddeliverymarketplace.entity.auth.AuthPermission;
import com.example.fooddeliverymarketplace.entity.auth.AuthRole;
import com.example.fooddeliverymarketplace.entity.auth.AuthUser;
import com.example.fooddeliverymarketplace.repository.authrepository.AuthPermissionRepository;
import com.example.fooddeliverymarketplace.repository.authrepository.AuthRoleRepository;
import com.example.fooddeliverymarketplace.repository.authrepository.AuthUserRepository;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserRepository userRepository;
    private final AuthPermissionRepository permissionRepository;
    private final AuthRoleRepository roleRepository;

    public CustomUserDetailsService(AuthUserRepository userRepository, AuthPermissionRepository permissionRepository, AuthRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + "not found"));
        List<AuthRole> authRoles = roleRepository.findAuthRolesByUserId(authUser.getId());

        for (AuthRole role : authRoles){
            List<AuthPermission> authPermissions = permissionRepository.findAuthPermissionsByRoleId(role.getId());
            role.setPermissions(authPermissions);
        }

        authUser.setRoles(authRoles);

        return new CustomUserDetails(authUser);
    }
}
