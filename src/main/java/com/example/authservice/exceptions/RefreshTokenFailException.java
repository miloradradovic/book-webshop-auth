package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class RefreshTokenFailException extends BadRequestException {

    public RefreshTokenFailException() {
        super("Refreshing token failed!");
    }
}
