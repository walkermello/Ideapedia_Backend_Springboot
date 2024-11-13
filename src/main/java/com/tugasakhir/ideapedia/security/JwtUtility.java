package com.tugasakhir.ideapedia.security;

import com.tugasakhir.ideapedia.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60; // 1 hour

    // Method to extract the token from the Authorization header
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove the "Bearer " prefix
        }

        return null; // Return null if token is not found
    }

    // Method to map claims from the token into a Map
    public Map<String, Object> mappingBodyToken(String token, Map<String, Object> mapz) {
        Claims claims = getAllClaimsFromToken(token);
        mapz.put("userId", claims.get("uid"));
        mapz.put("userName", claims.get("un"));
        mapz.put("nip", claims.get("nip"));
        mapz.put("email", claims.get("ml"));
        mapz.put("password", claims.get("pw"));
        mapz.put("noHp", claims.get("pn"));
        return mapz;
    }

    // Extract the username from the token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract the user ID from the token
    public Long getUserIdFromToken(String token) {
        // Dekripsi token yang terenkripsi
        String decryptedToken = Crypto.performDecrypt(token);  // Mendekripsi token

        Claims claims = Jwts.parser()
                .setSigningKey(JwtConfig.getJwtSecret())  // Use your secret key
                .parseClaimsJws(decryptedToken)
                .getBody();
        return claims.get("uid", Long.class);  // Extract user ID as Long
    }

    // Extract the expiration date from the token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Generic method to extract claims from the token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Parse the claims from the token using the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConfig.getJwtSecret())  // Use the secret key
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate a token for the user
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        claims = (claims == null) ? new HashMap<String, Object>() : claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Helper method to create the JWT token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long timeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(timeMillis))
                .setExpiration(new Date(timeMillis + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, JwtConfig.getJwtSecret())  // Use the secret key
                .compact();
    }

    // Validate the token by checking expiration and username
    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }
}
