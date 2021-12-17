package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {

    public UserAlreadyExistsException() {
        super("User with these credentials already exists!");
    }
}
