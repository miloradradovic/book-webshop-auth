package com.example.authservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class TokenUtils {

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Value("${jwt.secretKey}")
    public String SECRET;

    @Value("${jwt.expiresIn}") // 5h
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setAudience("web")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRES_IN))
                .claim("email", email)
                .claim("role", role)
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }
}
