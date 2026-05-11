package com.example.fooddeliverymarketplace.service.userdetails;


import com.example.fooddeliverymarketplace.entity.auth.AuthPermission;
import com.example.fooddeliverymarketplace.entity.auth.AuthRole;
import com.example.fooddeliverymarketplace.entity.auth.AuthUser;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record CustomUserDetails(AuthUser authUser) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AuthRole authRole : authUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authRole.getCode()));
            for (AuthPermission authPermission : authRole.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + authPermission.getCode()));
            }
        }
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
