package uk.co.huntersix.spring.rest.exception;

import java.util.List;

public final class ValidationErrorResponse {
  final List<Violation> violations;

  public ValidationErrorResponse(List<Violation> violations) {
    this.violations = violations;
  }
}
