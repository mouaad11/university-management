package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.exception.ResourceNotFoundException;
import com.etablissement.fullstack_backend.model.User;
import com.etablissement.fullstack_backend.repository.UserRepository;
import com.etablissement.fullstack_backend.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenFilter authTokenFilter;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/confirm-user/{userId}")
    public ResponseEntity<?> confirmUserAccount(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsConfirmed(true);
        userRepository.save(user);

        return ResponseEntity.ok("User account confirmed successfully!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
    @GetMapping("/api/auth/role")
    public ResponseEntity<String> getUserRole(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        String jwtToken = token.substring(7);
        try {
            // Add debug logging
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Current Authentication: " + auth);
            System.out.println("Authorities: " + auth.getAuthorities());

            String role = authTokenFilter.getJwtUtils().getSingleRoleFromJwtToken(jwtToken);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No roles found in token");
            }
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error processing JWT: " + e.getMessage());
        }
    }




}