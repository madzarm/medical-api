package com.example.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        String generalCode = "MEDICAL-SERVICE";
        int generalStatus = 500;
        ExceptionResponse response;
        if(e instanceof WebApplicationException) {
            try {
                response = ((WebApplicationException)e).getResponse().readEntity(ExceptionResponse.class);
            } catch (Exception ex){
                response = new ExceptionResponse(generalCode, ex.getMessage());
                return Response.status(generalStatus).entity(response).build();
            }
            return Response.status(generalStatus).entity(response).build();
        }
        response = new ExceptionResponse(generalCode, e.getMessage());
        return Response.status(generalStatus).entity(response).build();
    }
}