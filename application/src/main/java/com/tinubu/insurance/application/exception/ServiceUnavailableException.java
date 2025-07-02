package com.tinubu.insurance.application.exception;

public class ServiceUnavailableException extends RuntimeException {
  public ServiceUnavailableException(String message) {
    super(message);
  }

  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
