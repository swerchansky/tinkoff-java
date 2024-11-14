package edu.java.client.exception;

import edu.java.controller.dto.ApiErrorResponse;

public class ApiErrorException extends RuntimeException {
    public final ApiErrorResponse response;

    public ApiErrorException(String message) {
        super(message);
        this.response = null;
    }

    public ApiErrorException(String message, ApiErrorResponse response) {
        super(message);
        this.response = response;
    }
}
