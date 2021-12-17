package com.example.authservice.service;

import com.example.authservice.model.User;

public interface IUserService {

    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    User findByEmailOrPhoneNumber(String email, String phoneNumber);
    User saveOne(User user);
}
