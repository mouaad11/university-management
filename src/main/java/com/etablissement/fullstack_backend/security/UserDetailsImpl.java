package com.etablissement.fullstack_backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.etablissement.fullstack_backend.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean isConfirmed;  // New attribute
    private String role;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor to initialize all attributes including isConfirmed
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Boolean isConfirmed, String role,  Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isConfirmed = isConfirmed;
        this.role = role;
        this.authorities = authorities;

    }

    // Updated build method to include isConfirmed
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getIsConfirmed(),  // Add isConfirmed here
                user.getRole().toString(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;  // Return the actual authorities
    }

    @Override
    public String getPassword() {
        return password;  // Return the actual password
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;  // Return the actual username
    }

    // Getter and Setter for isConfirmed
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
