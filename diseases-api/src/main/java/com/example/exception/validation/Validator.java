package com.example.exception.validation;

import com.example.service.request.CreateDiseaseRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Validator implements ConstraintValidator<ValidateRequest, Object> {

    @Override
    public void initialize(ValidateRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        constraintValidatorContext.disableDefaultConstraintViolation();

        if(object instanceof CreateDiseaseRequest){
            CreateDiseaseRequest request = (CreateDiseaseRequest) object;

            if(!request.getName().matches("^[0-9a-zA-Z _]+$")) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Special characters are not allowed.")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                isValid = false;
            }
            if(request.getCurable() == null) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Curable may not be null")
                        .addPropertyNode("curable")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }
}
