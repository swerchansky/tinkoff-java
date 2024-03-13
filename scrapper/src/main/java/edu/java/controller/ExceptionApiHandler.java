package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
import edu.java.controller.exception.LastUpdateTimeUnresolvedException;
import edu.java.controller.exception.LinkNotSupportedException;
import edu.java.controller.exception.LinkNotTrackedException;
import java.util.stream.Stream;
import org.springframework.http.HttpStatusCode;
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

    @ExceptionHandler(LinkNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotSupportedException(LinkNotSupportedException exception) {
        return createCustomErrorResponse(exception);
    }

    @ExceptionHandler(LastUpdateTimeUnresolvedException.class)
    public ResponseEntity<ApiErrorResponse> handleTimeUnresolvedException(LastUpdateTimeUnresolvedException exception) {
        return createCustomErrorResponse(exception);
    }

    @ExceptionHandler(LinkNotTrackedException.class)
    public ResponseEntity<ApiErrorResponse> handleTimeUnresolvedException(LinkNotTrackedException exception) {
        return createCustomErrorResponse(exception);
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

    @SuppressWarnings("MagicNumber")
    private ResponseEntity<ApiErrorResponse> createCustomErrorResponse(Exception exception) {
        ApiErrorResponse response = createErrorResponse(exception, "406", exception.getMessage());
        return ResponseEntity.status(HttpStatusCode.valueOf(406)).body(response);
    }
}
