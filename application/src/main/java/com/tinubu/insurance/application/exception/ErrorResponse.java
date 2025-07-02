package com.tinubu.insurance.application.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Only include non-null fields
public class ErrorResponse {
  private String timestamp;
  private int status;
  private String error; // e.g., "Bad Request", "Not Found"
  private String message;
  private String path; // The request path that caused the error
  private List<ValidationError> validationErrors; // For validation issues
}
