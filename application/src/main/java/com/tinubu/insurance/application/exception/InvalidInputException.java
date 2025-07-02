package com.tinubu.insurance.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Optional: can be handled by @ExceptionHandler
public class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }

  public InvalidInputException(String message, Throwable cause) {
    super(message, cause);
  }
}
