package edu.java.bot.controller.exception;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException() {
        super("Too many requests");
    }
}
