package com.example.authservice.service;

import com.example.authservice.client.UserDataResponse;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.RegisterData;
import com.example.authservice.security.UserDetailsImpl;

public interface IAuthService {

    String login(LoginData loginData);
    void register(RegisterData registerData);
    UserDetailsImpl getUserDetails();
    UserDataResponse getUserData();
    String refreshToken(String accessToken);
}
