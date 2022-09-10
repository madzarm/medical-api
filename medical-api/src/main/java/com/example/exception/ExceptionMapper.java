package com.example.exception;


import com.example.exception.exceptions.BadCreateMedicalRecordRequestException;
import com.example.exception.exceptions.BadSearchMedicalRecordRequestException;
import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        ExceptionResponse response;

        if (e instanceof JsonParseException) {
            response = new ExceptionResponse("199", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        if (e instanceof BadSearchMedicalRecordRequestException) {
            response = new ExceptionResponse("191", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        if (e instanceof BadCreateMedicalRecordRequestException) {
            response = new ExceptionResponse("192", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        response = new ExceptionResponse("123", "Exception has occurred!");
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}