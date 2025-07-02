package com.tinubu.insurance.presentation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(
    validatedBy = {
      CreatePolicyDateRangeValidator.class,
      UpdatePolicyDateRangeValidator.class
    }) // Multiple validator classes here
@Target({ElementType.TYPE}) // Class-level validation
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPolicyDateRange {

  String message() default "End date must be after or equal to start date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
