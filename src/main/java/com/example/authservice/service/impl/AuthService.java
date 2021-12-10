package com.example.authservice.service.impl;

import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.model.LoginData;
import com.example.authservice.model.User;
import com.example.authservice.service.IAuthService;
import com.example.authservice.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @Override
    public String login(LoginData loginData) {
        User found = userService.findByUsernameAndPassword(loginData.getUsername(), loginData.getPassword());
        if (found == null) {
            throw new UserNotFoundException();
        }

        return tokenUtils.generateToken(found.getUsername(), found.getRoles().get(0));
    }
}
