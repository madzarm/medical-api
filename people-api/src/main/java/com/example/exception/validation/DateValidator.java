package com.example.exception.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<Date, String> {

    @Override
    public void initialize(Date constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }
        try {
            java.util.Date d = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            return true;
        } catch (Exception e) {
            try {
                java.util.Date d = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                return true;
            } catch (Exception e1) {
                try {
                    java.util.Date d = new SimpleDateFormat("dd.MM.yyyy").parse(date);
                    return true;
                } catch (Exception e2) {
                    try {
                        java.util.Date d = new SimpleDateFormat("ddMMyyyy").parse(date);
                        return true;
                    } catch (Exception e3) {
                        try {
                            java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            return true;
                        } catch (Exception e4) {
                            try {
                                java.util.Date d = new SimpleDateFormat("yyyy/MM/dd").parse(date);
                                return true;
                            } catch (Exception e5) {
                                try {
                                    java.util.Date d = new SimpleDateFormat("yyyy.MM.dd").parse(date);
                                    return true;
                                } catch (Exception e6) {
                                    try {
                                        java.util.Date d = new SimpleDateFormat("yyyyMMdd").parse(date);
                                        return true;
                                    } catch (Exception e7) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
