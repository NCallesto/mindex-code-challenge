package com.mindex.challenge.exception;

public class InvalidEmployeeRequestException extends RuntimeException{
    public InvalidEmployeeRequestException(String message) {
        super(message);
    }
}
