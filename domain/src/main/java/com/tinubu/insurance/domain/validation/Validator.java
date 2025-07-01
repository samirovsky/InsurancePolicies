package com.tinubu.insurance.domain.validation;

import com.tinubu.insurance.domain.policy.exception.PolicyValidationException;
import java.time.chrono.ChronoLocalDate;

public final class Validator {

  private Validator() {}

  public static <T> void requireNonNull(T fieldValue, String fieldName) {
    if (fieldValue == null) {
      throw new PolicyValidationException("%s is mandatory".formatted(fieldName));
    }
  }

  public static void requireNonNullAndNonBlank(String fieldValue, String fieldName) {
    if (fieldValue == null || fieldValue.isBlank()) {
      throw new PolicyValidationException("%s is mandatory".formatted(fieldName));
    }
  }

  public static <T extends ChronoLocalDate> void requireCoherentDatesPair(
      T leftFieldValue, String leftFieldName, T rightFiledValue, String rightFieldName) {
    if (leftFieldValue.isAfter(rightFiledValue)) {
      throw new PolicyValidationException(
          "%s must be before %s".formatted(leftFieldName, rightFieldName));
    }
  }
}
