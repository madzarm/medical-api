package com.example.exception.exceptions;

import javax.ws.rs.core.Response;

public class DiseaseServiceException extends DownstreamServiceException {


    public DiseaseServiceException(Response response) {
        super("Exception has occurred in a Disease service!", response);
    }

}
