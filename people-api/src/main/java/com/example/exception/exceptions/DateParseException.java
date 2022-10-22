package com.example.exception.exceptions;

public class DateParseException extends RuntimeException{

    public DateParseException() {
        super("Date has to be in formatted like: dd/MM/yyyy");
    }
}
