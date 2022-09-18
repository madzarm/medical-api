package com.example.exception.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Bad request!");
    }
}
