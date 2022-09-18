package com.example.exception.exceptions;

public class BadCreateMedicalRecordRequestException extends RuntimeException {
    public BadCreateMedicalRecordRequestException() {
        super("You may only provide diseaseIds OR diseaseName, not both!");
    }
}
