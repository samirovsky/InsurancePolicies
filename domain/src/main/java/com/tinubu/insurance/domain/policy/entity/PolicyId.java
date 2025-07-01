package com.tinubu.insurance.domain.policy.entity;

import static com.tinubu.insurance.domain.validation.Validator.requireNonNullAndNonBlank;

import java.util.UUID;

public record PolicyId(String value) {
  public PolicyId {
    requireNonNullAndNonBlank(value, "insuranceIdValue");
  }

  public static PolicyId fromUUID(UUID uuid) {
    return new PolicyId(uuid.toString());
  }

  public UUID toUUID() {
    return UUID.fromString(value);
  }
}
