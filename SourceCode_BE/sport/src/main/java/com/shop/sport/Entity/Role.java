package com.shop.sport.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Permission;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public enum Role {
  ADMIN(Collections.emptySet()), EMPLOYEE(Collections.emptySet()),CUSTOMER(Collections.emptySet());
  @Getter
  private final Set<Permission> permissions;


  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}