package com.example.exception.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Long id) {
        super("Entity with id: " + id + " was not found!");
    }

    public EntityNotFoundException() {
        super("Entity was not found!");
    }
}
