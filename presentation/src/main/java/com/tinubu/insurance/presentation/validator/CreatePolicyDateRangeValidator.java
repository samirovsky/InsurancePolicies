package com.tinubu.insurance.presentation.validator;

import com.tinubu.insurance.presentation.dto.CreatePolicyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreatePolicyDateRangeValidator
    implements ConstraintValidator<ValidPolicyDateRange, CreatePolicyRequest> {

  @Override
  public void initialize(ValidPolicyDateRange constraintAnnotation) {
    // Optional initialization logic
  }

  @Override
  public boolean isValid(CreatePolicyRequest request, ConstraintValidatorContext context) {
    if (request == null || request.startDate() == null || request.endDate() == null) {
      return true; // Let @NotNull handle null validation
    }
    return !request.endDate().isBefore(request.startDate());
  }
}
