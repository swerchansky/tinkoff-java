package edu.java.bot.client.exception;

import edu.java.bot.controller.dto.ApiErrorResponse;

public class ApiErrorException extends RuntimeException {
    public final ApiErrorResponse response;

    public ApiErrorException(String message, ApiErrorResponse response) {
        super(message);
        this.response = response;
    }
}
