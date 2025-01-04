package com.etablissement.fullstack_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = generateSigningKey();
        System.out.println("Initialized signing key with algorithm: " + signingKey.getAlgorithm());
    }

    private Key generateSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        try {
            System.out.println("Generating JWT token for user: " + userPrincipal.getUsername());

            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            System.out.println("User roles: " + roles);

            String token = Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .claim("role", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(signingKey, SignatureAlgorithm.HS256)
                    .compact();

            // Verify token structure after generation
            System.out.println("Generated token verification:");
            verifyTokenEncoding(token);

            return token;
        } catch (Exception e) {
            System.out.println("Error generating token: " + e.getMessage());
            throw new RuntimeException("Error generating token", e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("\nValidating JWT token...");

            if (!verifyTokenEncoding(authToken)) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(authToken);
            System.out.println("Token validation successful");
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private boolean verifyTokenEncoding(String token) {
        System.out.println("Token length: " + token.length());
        System.out.println("Token preview: " + token.substring(0, Math.min(token.length(), 20)) + "...");

        String[] parts = token.split("\\.");
        System.out.println("Token parts count: " + parts.length);

        if (parts.length != 3) {
            System.out.println("Invalid token structure: Expected 3 parts, got " + parts.length);
            return false;
        }

        // Use URL-safe Base64 decoder
        Base64.Decoder decoder = Base64.getUrlDecoder();

        for (int i = 0; i < parts.length; i++) {
            try {
                String part = parts[i];
                // Add padding if necessary
                while (part.length() % 4 != 0) {
                    part += "=";
                }
                decoder.decode(part);
                System.out.println("Part " + i + " is valid Base64URL");
            } catch (IllegalArgumentException e) {
                System.out.println("Part " + i + " is not valid Base64URL: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
    /**
     * Use a secure method to generate the key based on the provided secret
     * and ensure it is at least 256 bits long for HMAC-SHA256.
     */
    private Key key() {
        // If the jwtSecret length is less than 32 bytes (256 bits), generate a strong key using HMAC-SHA256
        if (jwtSecret.length() < 32) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            // Ensure the jwtSecret is 256 bits long
            return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }
    }



    /**
     * Extract the username from the JWT token.
     * @param token The JWT token.
     * @return The username from the token.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extract the user role from the JWT token.
     * @param token The JWT token.
     * @return The role from the token.
     */
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", List.class);
    }

    /**
     * Extract a single role from the JWT token.
     * @param token The JWT token.
     * @return The first role from the token, or null if no roles are present.
     */
    public String getSingleRoleFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Get the roles from the token
            List<String> roles = claims.get("role", List.class);

            // Return the first role if available
            if (roles != null && !roles.isEmpty()) {
                return roles.get(0);  // Return the first role
            }
        } catch (Exception e) {
            logger.error("Error extracting role from token: {}", e.getMessage());
        }
        return null;  // Return null if no roles were found or an error occurred
    }


}
