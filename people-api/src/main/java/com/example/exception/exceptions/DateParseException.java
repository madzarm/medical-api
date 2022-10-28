package com.example.exception.exceptions;

public class DateParseException extends RuntimeException{

    public DateParseException(String date) {
        super("Date: " + date + " could not be parsed!");
    }
}
