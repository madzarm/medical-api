package com.example.exception.exceptions;

public class DatabaseEmptyException extends RuntimeException{
    public DatabaseEmptyException() {
        super("Database is returning an empty response");
    }
}
