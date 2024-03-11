package edu.java.bot.controller;

import edu.java.bot.controller.dto.ApiErrorResponse;
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
