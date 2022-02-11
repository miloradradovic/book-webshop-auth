package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class CreateUserFailException extends BadRequestException {

    public CreateUserFailException() {
        super("Failed to create user!");
    }
}
