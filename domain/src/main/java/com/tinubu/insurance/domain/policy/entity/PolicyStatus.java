package com.tinubu.insurance.domain.policy.entity;

import com.tinubu.insurance.domain.policy.exception.PolicyValidationException;
import java.util.stream.Stream;

public enum PolicyStatus {
  ACTIVE,
  INACTIVE;

  public static PolicyStatus fromString(String value) {
    return Stream.of(values())
        .filter(status -> status.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new PolicyValidationException("Invalid policy status: " + value));
  }
}
