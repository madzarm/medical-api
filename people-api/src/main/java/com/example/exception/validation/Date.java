package com.example.exception.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface Date {

    String message() default "Date could not be parsed!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
