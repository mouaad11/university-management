package com.etablissement.fullstack_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("--- JWT Filter Debug Start ---");
            System.out.println("Request URL: " + request.getRequestURL().toString());

            String jwt = parseJwt(request);
            System.out.println("Extracted JWT: " + (jwt != null ? jwt.substring(0, Math.min(jwt.length(), 20)) + "..." : "null"));

            if (jwt != null) {
                System.out.println("Attempting to validate JWT token...");
                boolean isValid = jwtUtils.validateJwtToken(jwt);
                System.out.println("JWT validation result: " + isValid);

                if (isValid) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    System.out.println("Extracted username from JWT: " + username);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println("Loaded UserDetails: " + userDetails.getUsername());
                    System.out.println("User enabled: " + userDetails.isEnabled());
                    System.out.println("User non-expired: " + userDetails.isAccountNonExpired());
                    System.out.println("User credentials non-expired: " + userDetails.isCredentialsNonExpired());
                    System.out.println("User non-locked: " + userDetails.isAccountNonLocked());

                    // Extract roles from the JWT
                    List<String> roles = jwtUtils.getRolesFromJwtToken(jwt);
                    System.out.println("Extracted roles from JWT: " + roles);

                    Collection<? extends GrantedAuthority> authorities = roles.stream()
                            .map(role -> {
                                System.out.println("Converting role to authority: " + role);
                                return new SimpleGrantedAuthority(role);
                            })
                            .collect(Collectors.toList());
                    System.out.println("Created authorities: " + authorities);

                    // Create authentication token with roles
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("Created authentication token: " + authentication);

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Successfully set authentication in SecurityContext");
                }
            } else {
                System.out.println("No JWT token found in request");
            }
        } catch (Exception e) {
            System.out.println("=== Exception Details Start ===");
            System.out.println("Exception message: " + e.getMessage());
            System.out.println("Exception class: " + e.getClass().getName());
            e.printStackTrace();
            System.out.println("=== Exception Details End ===");
            logger.error("Cannot set user authentication: {}", e);
        } finally {
            System.out.println("--- JWT Filter Debug End ---");
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
