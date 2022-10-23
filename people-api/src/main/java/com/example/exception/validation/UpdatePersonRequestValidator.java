package com.example.exception.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UpdatePersonRequestValidator implements ConstraintValidator<UpdatePersonRequestValidation, com.example.service.request.UpdatePersonRequest> {

    @Override
    public void initialize(UpdatePersonRequestValidation annotation) {}

    @Override
    public boolean isValid(com.example.service.request.UpdatePersonRequest request, ConstraintValidatorContext constraintValidatorContext) {
        boolean hasDHId = Objects.nonNull(request.getDiseaseHistoryId());
        boolean hasDiseaseId = Objects.nonNull(request.getDiseaseId());
        boolean hasDate = Objects.nonNull(request.getDateDiscovered());

        return hasDHId == (hasDiseaseId || hasDate);
    }
}
