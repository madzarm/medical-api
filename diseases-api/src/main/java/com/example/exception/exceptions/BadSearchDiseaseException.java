package com.example.exception.exceptions;

public class BadSearchDiseaseException extends RuntimeException{
    public BadSearchDiseaseException() {
        super("You may search a disease by it's id, name, and by curable. You may not combine any of the former.");
    }
}
