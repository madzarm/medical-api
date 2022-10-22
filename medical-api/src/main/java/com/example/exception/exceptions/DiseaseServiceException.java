package com.example.exception.exceptions;

import javax.ws.rs.core.Response;

public class DiseaseServiceException extends RuntimeException {


    public DiseaseServiceException() {
        super("Exception has occurred in a Disease service!");
    }

}
