package com.example.authservice.service;

import com.example.authservice.model.LoginData;

public interface IAuthService {

    String login(LoginData loginData);
}
