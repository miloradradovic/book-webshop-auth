package com.example.authservice.service.impl;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.exceptions.*;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.RegisterData;
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
            return jwtUtils.generateJwtToken(authentication);
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
    public void register(RegisterData registerData) {
        User alreadyRegistered = userService.findByEmailOrPhoneNumber(registerData.getEmail(), registerData.getPhoneNumber());
        if (alreadyRegistered != null) {
            throw new UserAlreadyExistsException();
        }
        User saved = userService.saveOne(new User(registerData.getEmail(), passwordEncoder.encode(registerData.getPassword()), registerData.getName(), registerData.getSurname(), registerData.getPhoneNumber(), registerData.getAddress()));
        if (saved == null) {
            throw new RegistrationFailException();
        }
    }

    @Override
    public UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDataResponse getUserData() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User found = userService.findByEmail(userDetails.getUsername());
        if (found == null) {
            throw new UserNotFoundException();
        }
        return new UserDataResponse(found.getAddress(), found.getPhoneNumber());
    }
}
