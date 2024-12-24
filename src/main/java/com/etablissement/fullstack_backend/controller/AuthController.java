package com.etablissement.fullstack_backend.controller;

import com.etablissement.fullstack_backend.model.*;
import com.etablissement.fullstack_backend.repository.*;
import com.etablissement.fullstack_backend.payload.request.LoginRequest;
import com.etablissement.fullstack_backend.payload.request.SignupRequest;
import com.etablissement.fullstack_backend.payload.response.JwtResponse;
import com.etablissement.fullstack_backend.repository.UserRepository;
import com.etablissement.fullstack_backend.security.JwtUtils;
import com.etablissement.fullstack_backend.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Ensure that the principal is of type UserDetailsImpl
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Check if user is confirmed
        if (!userDetails.getIsConfirmed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: Account is not confirmed yet!");
        }

        String jwt = jwtUtils.generateJwtToken(userDetails);  // Pass the UserDetailsImpl directly

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create user based on role
        User user;

        switch(signUpRequest.getRole()) {
            case ROLE_STUDENT:
                Student student = new Student();
                student.setStudentId(signUpRequest.getStudentId());
                if (signUpRequest.getClassId() != null) {

                    Classe classe = classeRepository.findById(signUpRequest.getClassId())
                            .orElseThrow(() -> new RuntimeException("Error: Class not found."));
                    student.setClasse(classe);
                }
                user = student;
                break;

            case ROLE_PROFESSOR:
                Professor professor = new Professor();
                professor.setDepartment(signUpRequest.getDepartment());
                user = professor;
                break;

            case ROLE_ADMIN:
                user = new Administrator();
                break;

            default:
                return ResponseEntity.badRequest().body("Error: Invalid role specified.");
        }

        // Set common user fields
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setRole(signUpRequest.getRole());
        user.setIsConfirmed(false);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully! Please wait for admin confirmation.");
    }
}
