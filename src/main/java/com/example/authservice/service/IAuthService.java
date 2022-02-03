package com.example.authservice.service;

import com.example.authservice.model.LoginData;
import com.example.authservice.model.TokenData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;

public interface IAuthService {

    TokenData login(LoginData loginData);
    User register(User toRegister);
    UserDetailsImpl getCurrentlyLoggedIn();
    TokenData refreshToken(String accessToken);
}
