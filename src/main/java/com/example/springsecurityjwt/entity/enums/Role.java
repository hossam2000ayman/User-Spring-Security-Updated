package com.example.springsecurityjwt.entity.enums;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.springsecurityjwt.entity.enums.Permissions.*;

@Getter
public enum Role {
    USER("USER", Set.of(USER_READ, USER_UPDATE, USER_DELETE)), ADMIN("ADMIN", Set.of(ADMIN_CREATE, ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, USER_READ, USER_UPDATE, USER_DELETE));

    private final Set<Permissions> permissions;
    private final String role;

    Role(String role, Set<Permissions> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority(this.name()));
        return authorities;
    }
}
