package edu.java.bot.client.exception;

public class ApiErrorException extends RuntimeException {
    public ApiErrorException(String message) {
        super(message);
    }
}
