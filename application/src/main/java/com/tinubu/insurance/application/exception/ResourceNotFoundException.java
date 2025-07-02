package com.tinubu.insurance.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Optional: can be handled by @ExceptionHandler
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
