package com.example.exception.exceptions;

import javax.ws.rs.core.Response;

public class DownstreamServiceException extends RuntimeException {

    private Response response;

    public DownstreamServiceException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
