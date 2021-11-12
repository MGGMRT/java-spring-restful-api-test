package uk.co.huntersix.spring.rest.exception;

public final class Violation {
  public final String fieldName;
  public final String message;

  public Violation(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;
  }
}
