package edu.java.controller.exception;

public class LinkNotTrackedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Link is not being tracked";
    }
}
