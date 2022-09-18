package com.example.exception.exceptions;

public class BadSearchMedicalRecordRequestException extends RuntimeException {
    public BadSearchMedicalRecordRequestException() {
        super("You may not combine diseaseName with personName, diseaseIds nad personIds");
    }
}
