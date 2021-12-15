package com.example.authservice.service.impl;

import com.example.authservice.model.LoginData;
import com.example.authservice.service.IAuthService;
import com.example.authservice.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthService implements IAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    @Override
    public String login(LoginData loginData) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User fromContext = (User) authentication.getPrincipal();
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) fromContext.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            return tokenUtils.generateToken(loginData.getEmail(), authority.toString());
        }
        return null;
    }
}
