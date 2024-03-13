package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        ApiErrorResponse response = createErrorResponse(exception, "400", "Validation error");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(IllegalArgumentException exception) {
        ApiErrorResponse response = createErrorResponse(exception, "406", "Link not supported");
        return ResponseEntity.badRequest().body(response);
    }

    private ApiErrorResponse createErrorResponse(Exception exception, String code, String description) {
        return new ApiErrorResponse(
                description,
                code,
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                Stream.of(exception.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
