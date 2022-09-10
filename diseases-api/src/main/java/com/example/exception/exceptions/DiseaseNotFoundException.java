package com.example.exception.exceptions;

public class DiseaseNotFoundException extends RuntimeException{
    public DiseaseNotFoundException() {
        super("Disease not found!");
    }

    public DiseaseNotFoundException(Long id) {
        super("Disease with id: " + id + " not found!");
    }
}
