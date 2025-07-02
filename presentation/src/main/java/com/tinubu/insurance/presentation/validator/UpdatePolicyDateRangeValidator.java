package com.tinubu.insurance.presentation.validator;

import com.tinubu.insurance.presentation.dto.UpdatePolicyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdatePolicyDateRangeValidator
    implements ConstraintValidator<ValidPolicyDateRange, UpdatePolicyRequest> {

  @Override
  public void initialize(ValidPolicyDateRange constraintAnnotation) {
    // Optional initialization logic
  }

  @Override
  public boolean isValid(UpdatePolicyRequest request, ConstraintValidatorContext context) {
    if (request == null || request.startDate() == null || request.endDate() == null) {
      return true; // Let @NotNull handle null validation
    }
    return !request.endDate().isBefore(request.startDate());
  }
}
