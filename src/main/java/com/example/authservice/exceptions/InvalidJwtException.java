package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.ForbiddenException;

public class InvalidJwtException extends ForbiddenException {

    public InvalidJwtException() {
        super("Your jwt token is invalid!");
    }
}
