package com.example.service.result;


import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ValidationResult {

    private String message;
    private boolean success;

    public ValidationResult(String message) {
        this.success = true;
        this.message = message;
    }

    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
        this.message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
