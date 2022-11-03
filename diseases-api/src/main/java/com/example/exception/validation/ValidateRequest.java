package com.example.exception.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = Validator.class)
@Documented
public @interface ValidateRequest {
    String message() default "You must have either diseaseId and/or dateDiscovered with diseaseHistoryId, they may not be null!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
