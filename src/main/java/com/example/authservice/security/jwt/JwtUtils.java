package com.example.authservice.security.jwt;

import com.example.authservice.exceptions.InvalidJwtException;
import com.example.authservice.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.expiresIn}")
    private int jwtExpirationMs;

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception exception) {
            throw new InvalidJwtException();
        }
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl)
                authentication.getPrincipal();
        String role = "";
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            role = authority.getAuthority();
        }
        return Jwts.builder().setSubject((userDetails.getUsername())).
                setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() +
                        jwtExpirationMs))
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String refreshToken(String accessToken) {
        try {
            final Claims claims = this.getAllClaimsFromToken(accessToken);
            claims.setIssuedAt(new Date());
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date((new Date()).getTime() +
                            jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        } catch (Exception e) {
            return null;
        }
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).
                getBody().getSubject();
    }

    public String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
    }
}
