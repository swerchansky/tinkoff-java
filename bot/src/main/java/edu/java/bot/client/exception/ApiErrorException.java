package edu.java.bot.client.exception;

public class ApiErrorException extends RuntimeException {
    public ApiErrorException(String message, int statusCode) {
        super(message + " with status code: " + statusCode);
    }
}
