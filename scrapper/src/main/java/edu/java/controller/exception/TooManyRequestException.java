package edu.java.controller.exception;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException() {
        super("Too many requests");
    }
}
