package com.example.exception.exceptions;

public class DiseaseAlreadyExistsException extends RuntimeException {
    public DiseaseAlreadyExistsException(Long id) {
        super("DiseaseHistory with diseaseId: " + id + " already exists!");
    }

    public DiseaseAlreadyExistsException() {
        super("Disease with that id already exists!");
    }
}
