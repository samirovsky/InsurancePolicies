package com.tinubu.insurance.domain.policy.exception;

public class PolicyValidationException extends RuntimeException {
  public PolicyValidationException(String message) {
    super(message);
  }
}
