package com.tinubu.insurance.application.exception; // Or

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @ExceptionHandler(payloadType = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      InvalidInputException ex, HttpServletRequest request) {
    LOGGER.error("MethodArgumentNotValid: {}", ex.getMessage());
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request.getRequestURI());
  }

  /**
   * Handles custom ResourceNotFoundException (404 Not Found) Thrown when a requested resource does
   * not exist.
   */
  @ExceptionHandler(payloadType = ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, HttpServletRequest request) {
    LOGGER.warn("Resource Not Found: {}", ex.getMessage());
    return buildErrorResponse(
        HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI());
  }

  /**
   * Handles custom InvalidInputException (400 Bad Request) Thrown for general bad input that
   * doesn't fit specific validation.
   */
  @ExceptionHandler(payloadType = InvalidInputException.class)
  public ResponseEntity<ErrorResponse> handleInvalidInputException(
      InvalidInputException ex, HttpServletRequest request) {
    LOGGER.warn("Invalid Input: {}", ex.getMessage());
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request.getRequestURI());
  }

  /**
   * Handles MethodArgumentNotValidException (400 Bad Request) Thrown when @Valid or @Validated
   * fails for request body.
   */
  @ExceptionHandler(payloadType = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<ValidationError> validationErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    ValidationError.builder()
                        .field(fieldError.getField())
                        .defaultMessage(fieldError.getDefaultMessage())
                        .build())
            .collect(Collectors.toList());

    String message = "Validation failed for request parameters.";
    LOGGER.warn("{}: {}", message, validationErrors);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        message,
        request.getRequestURI());
  }

  /**
   * Handles MethodArgumentTypeMismatchException (400 Bad Request) Thrown when method argument is
   * not of the expected type (e.g., UUID in path variable).
   */
  @ExceptionHandler(payloadType = MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    String message =
        String.format(
            "Parameter '%s' has invalid value '%s'. Expected type '%s'.",
            ex.getName(),
            ex.getValue(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
    LOGGER.warn("Type Mismatch: {}", message);
    return buildErrorResponse(
        HttpStatus.BAD_REQUEST, "Bad Request", message, request.getRequestURI());
  }

  /**
   * Handles custom ServiceUnavailableException (503 Service Unavailable) Thrown for external
   * service dependencies issues or temporary outages.
   */
  @ExceptionHandler(payloadType = ServiceUnavailableException.class)
  public ResponseEntity<ErrorResponse> handleServiceUnavailableException(
      ServiceUnavailableException ex, HttpServletRequest request) {
    LOGGER.error(
        "Service Unavailable: {}", ex.getMessage(), ex); // Log full stack trace for service issues
    return buildErrorResponse(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Service Unavailable",
        ex.getMessage(),
        request.getRequestURI());
  }

  /**
   * Handles NoHandlerFoundException (404 Not Found for undefined endpoints) Make sure
   * 'spring.mvc.throw-exception-if-no-handler-found=true' is set in application.properties.
   */
  @ExceptionHandler(payloadType = NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpServletRequest request) {
    LOGGER.warn("No Handler Found: {} {}", ex.getHttpMethod(), ex.getRequestURL());
    return buildErrorResponse(
        HttpStatus.NOT_FOUND,
        "Not Found",
        "API endpoint not found: " + request.getRequestURI(),
        request.getRequestURI());
  }

  /**
   * General fallback for any unhandled exceptions (500 Internal Server Error) Logs the full stack
   * trace for debugging.
   */
  @ExceptionHandler(payloadType = Exception.class)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(
      Exception ex, HttpServletRequest request) {
    LOGGER.error("An unexpected internal server error occurred: ", ex); // Log full stack trace
    return buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Internal Server Error",
        "An unexpected error occurred. Please try again later.",
        request.getRequestURI());
  }

  /** Builds a standardized ErrorResponse object. */
  private ResponseEntity<ErrorResponse> buildErrorResponse(
      HttpStatus status, String error, String message, String path) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now().format(FORMATTER))
            .status(status.value())
            .error(error)
            .message(message)
            .path(path)
            .build();
    return new ResponseEntity<>(errorResponse, status);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      HttpStatus status,
      String error,
      String message,
      String path,
      List<ValidationError> validationErrors) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now().format(FORMATTER))
            .status(status.value())
            .error(error)
            .message(message)
            .path(path)
            .validationErrors(validationErrors)
            .build();
    return new ResponseEntity<>(errorResponse, status);
  }
}
