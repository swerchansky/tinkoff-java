package edu.java.controller.exception;

public class LastUpdateTimeUnresolvedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Unable to get last update time for the link";
    }
}
