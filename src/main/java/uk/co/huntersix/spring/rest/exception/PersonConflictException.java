package uk.co.huntersix.spring.rest.exception;

public class PersonConflictException extends RuntimeException {
  public PersonConflictException(String s) {
    super(s);
  }
}
