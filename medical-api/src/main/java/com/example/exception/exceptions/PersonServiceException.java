package com.example.exception.exceptions;


public class PersonServiceException extends RuntimeException {

    public PersonServiceException() {
        super("Exception has occurred in a Person service!");
    }
}
