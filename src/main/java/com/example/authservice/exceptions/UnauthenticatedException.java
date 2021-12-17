package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.UnauthorizedException;

public class UnauthenticatedException extends UnauthorizedException {

    public UnauthenticatedException() {
        super("Bad credentials!");
    }
}
