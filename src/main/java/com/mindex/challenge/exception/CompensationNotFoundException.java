package com.mindex.challenge.exception;

/**
 * Exception thrown when no compensation record is found for a requested employee.
 */
public class CompensationNotFoundException extends Exception {
    /**
     * Constructs a new CompensationNotFoundException with the specified detail message.
     *
     * @param message The detail message
     */
    public CompensationNotFoundException(String message) {
        super(message);
    }
}