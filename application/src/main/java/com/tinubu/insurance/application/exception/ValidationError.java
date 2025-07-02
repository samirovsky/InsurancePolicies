package com.tinubu.insurance.application.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationError {
  private String field;
  private String defaultMessage;
}
