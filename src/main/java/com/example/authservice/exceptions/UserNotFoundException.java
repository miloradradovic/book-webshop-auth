package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class UserNotFoundException extends BadRequestException {

    public UserNotFoundException() {
        super("User not found!");
    }
}
