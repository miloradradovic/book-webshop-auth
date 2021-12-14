package com.example.authservice.service;

import com.example.authservice.model.User;

public interface IUserService {

    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
