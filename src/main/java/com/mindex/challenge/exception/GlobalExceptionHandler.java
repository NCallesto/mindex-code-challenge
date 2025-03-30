package com.mindex.challenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Centralizes exception handling and provides consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles InvalidEmployeeRequestException by returning the 400 Bad Request response.
     * 
     * @param ex The caught exception
     * @return ResponseEntity with an error message and the HTTP status
     */
    @ExceptionHandler(InvalidEmployeeRequestException.class)
    public ResponseEntity<String> handleInvalidEmployeeRequest(InvalidEmployeeRequestException ex) {
        LOG.warn("Invalid employee request: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EmployeeNotFoundException by returning the 404 Not Found response.
     * 
     * @param ex The caught exception
     * @return ResponseEntity with an error message and the HTTP status
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        LOG.warn("Employee not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other exceptions by returning a 500 Internal Server Error response.
     * 
     * @param ex The caught exception
     * @return ResponseEntity with a generic error message and the HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        LOG.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}