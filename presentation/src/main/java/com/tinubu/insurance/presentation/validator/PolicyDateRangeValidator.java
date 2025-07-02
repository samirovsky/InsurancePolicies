package com.tinubu.insurance.presentation.validator;

import com.tinubu.insurance.presentation.dto.UpdatePolicyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PolicyDateRangeValidator
    implements ConstraintValidator<ValidPolicyDateRange, UpdatePolicyRequest> {

  @Override
  public boolean isValid(UpdatePolicyRequest request, ConstraintValidatorContext context) {
    if (request == null) {
      return true; // or false depending on your null policy
    }
    if (request.startDate() == null || request.endDate() == null) {
      return true; // @NotNull should handle null checks separately
    }
    return !request.endDate().isBefore(request.startDate());
  }
}
