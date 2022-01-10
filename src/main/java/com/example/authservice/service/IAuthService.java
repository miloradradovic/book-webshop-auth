package com.example.authservice.service;

import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.security.UserDetailsImpl;

public interface IAuthService {

    String login(LoginData loginData);
    User register(User toRegister);
    UserDetailsImpl getCurrentlyLoggedIn();
    String refreshToken(String accessToken);
}
