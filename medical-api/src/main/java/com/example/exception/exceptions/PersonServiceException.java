package com.example.exception.exceptions;

import javax.ws.rs.core.Response;

public class PersonServiceException extends DownstreamServiceException{

    public PersonServiceException(Response response) {
        super("Exception has occurred in a Person service!", response);
    }
}
