package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class RegistrationFailException extends BadRequestException {

    public RegistrationFailException() {
        super("Something went wrong with the database while registering!");
    }
}
