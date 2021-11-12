package uk.co.huntersix.spring.rest.exception;

public class PersonNotFoundException extends RuntimeException {
  public PersonNotFoundException(String s) {
    super(s);
  }
}
