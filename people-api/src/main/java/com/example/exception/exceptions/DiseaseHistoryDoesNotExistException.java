package com.example.exception.exceptions;

public class DiseaseHistoryDoesNotExistException extends RuntimeException{
    public DiseaseHistoryDoesNotExistException(Long id) {
        super("Disease history with id: \"" + id + "\" doesnt exist!");
    }

}
