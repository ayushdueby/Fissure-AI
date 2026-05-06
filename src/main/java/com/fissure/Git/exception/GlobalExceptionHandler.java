package com.fissure.Git.exception;

import com.fissure.Git.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        ApiResponse response = ApiResponse.error(
                "Missing required query parameter: " + ex.getParameterName(),
                "MISSING_QUERY_PARAM"
        );
        response.data = java.util.Map.of("path", request.getRequestURI(), "parameter", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        ApiResponse response = ApiResponse.error(ex.getMessage(), "VALIDATION_ERROR");
        response.data = java.util.Map.of("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = switch (ex.getMessage()) {
            case "BRANCH_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "CANNOT_DELETE_CURRENT_BRANCH", "BRANCH_NOT_MERGED", "NOTHING_TO_COMMIT" -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ApiResponse response = ApiResponse.error(ex.getMessage(), "RUNTIME_ERROR");
        response.data = java.util.Map.of("path", request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiResponse response = ApiResponse.error("Internal server error", "INTERNAL_ERROR");
        response.data = java.util.Map.of("path", request.getRequestURI(), "detail", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
