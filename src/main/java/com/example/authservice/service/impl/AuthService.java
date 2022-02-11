package com.example.authservice.service.impl;

import com.example.authservice.exceptions.*;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.TokenData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;
import com.example.authservice.security.jwt.JwtUtils;
import com.example.authservice.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements IAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Override
    public TokenData login(LoginData loginData) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtils.generateAccessToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            return new TokenData(accessToken, refreshToken);
        } catch (Exception e) {
            throw new UnauthenticatedException();
        }
    }

    @Override
    public TokenData refreshToken(String refreshToken) {
        String newAccessToken = jwtUtils.refreshToken(refreshToken);
        String newRefreshToken = jwtUtils.refreshToken(newAccessToken);
        if (newAccessToken == null || newRefreshToken == null) {
            throw new RefreshTokenFailException();
        }
        return new TokenData(newAccessToken, newRefreshToken);
    }

    @Override
    public User register(User toRegister) {
        List<User> alreadyRegistered = userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        if (!alreadyRegistered.isEmpty()) {
            throw new UserAlreadyExistsException();
        }
        toRegister.setPassword(passwordEncoder.encode(toRegister.getPassword()));
        try {
            return userService.createThrowsException(toRegister);
        } catch (Exception e) {
            throw new RegistrationFailException();
        }
    }

    @Override
    public UserDetailsImpl getCurrentlyLoggedIn() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
