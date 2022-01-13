package com.example.authservice.service.impl;

import com.example.authservice.exceptions.*;
import com.example.authservice.model.LoginData;
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
    public String login(LoginData loginData) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            return jwt;
        } catch (Exception e) {
            throw new UnauthenticatedException();
        }
    }

    @Override
    public String refreshToken(String accessToken) {
        String newToken = jwtUtils.refreshToken(accessToken);
        if (newToken == null) {
            throw new RefreshTokenFailException();
        }
        return newToken;
    }

    @Override
    public User register(User toRegister) {
        List<User> alreadyRegistered = userService.getByEmailOrPhoneNumber(toRegister.getEmail(), toRegister.getPhoneNumber());
        if (alreadyRegistered.size() != 0) {
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
