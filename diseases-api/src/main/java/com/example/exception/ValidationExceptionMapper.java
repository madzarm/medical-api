package com.example.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private String getPropertyName(Path path) {
        String[] split = path.toString().split("[.]");
        return split[split.length - 1];
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> getPropertyName(v.getPropertyPath()), ConstraintViolation::getMessage));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errors).type(MediaType.APPLICATION_JSON)
                .build();
    }
}

