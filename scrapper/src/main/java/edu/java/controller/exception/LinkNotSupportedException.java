package edu.java.controller.exception;

public class LinkNotSupportedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Link not supported";
    }
}
