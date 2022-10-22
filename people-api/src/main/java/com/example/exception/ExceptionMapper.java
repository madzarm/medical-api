package com.example.exception;

import com.example.exception.exceptions.*;
import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        ExceptionResponse response;
        int generalStatus = 500;
        String generalCode = "PERSON-SERVICE";

        response = new ExceptionResponse(generalCode, e.getMessage());
        return Response.status(generalStatus).entity(response).build();
    }
}