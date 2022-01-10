package com.example.authservice.exceptions;

import com.example.authservice.exceptions.general.BadRequestException;

public class DeleteUserFailException extends BadRequestException {

    public DeleteUserFailException() {
        super("Failed to delete user!");
    }
}
