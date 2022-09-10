package com.example.exception;
import com.example.exception.exceptions.*;
import com.fasterxml.jackson.core.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        ExceptionResponse response;

        if (e instanceof EntityNotFoundException) {
            response = new ExceptionResponse("113", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        if (e instanceof BadSearchDiseaseException) {
            response = new ExceptionResponse("112", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        if (e instanceof DiseaseNotFoundException) {
            response = new ExceptionResponse("111", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        if(e instanceof DatabaseEmptyException) {
            response = new ExceptionResponse("151", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        if(e instanceof BadRequestException) {
            response = new ExceptionResponse("121", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        if (e instanceof JsonParseException) {
            response = new ExceptionResponse("199", e.getMessage());
            return Response.status(202).entity(response).build();
        }
        response = new ExceptionResponse("123", "Exception has occurred!");
        e.printStackTrace();
        return Response.status(202).entity(response).build();
    }
}