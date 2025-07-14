package com.paybook.domain.exception;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String fieldName) {
        super("Required field is missing or invalid: " + fieldName);
    }
}