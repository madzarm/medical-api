package com.example.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        ExceptionResponse response;
        int generalStatus = 500;
        String generalCode = "DISEASE-SERVICE";

        response = new ExceptionResponse(generalCode, e.getMessage());
        return Response.status(generalStatus).entity(response).build();
    }
}