package uk.co.huntersix.spring.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e) {
    List<Violation> violations =
        e.getBindingResult().getAllErrors().stream()
            .filter(FieldError.class::isInstance)
            .map(FieldError.class::cast)
            .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    return ResponseEntity.badRequest().body(new ValidationErrorResponse(violations));
  }

  @ExceptionHandler(PersonNotFoundException.class)
  public ResponseEntity<Object> onPersonNotFoundException(PersonNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(PersonConflictException.class)
  public ResponseEntity<Object> onPersonConflictException(PersonConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler(PersonSearchContentNotFound.class)
  public ResponseEntity<Object> onPersonSearchContentNotFound(PersonSearchContentNotFound e) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> onException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Unexpected Error!, please make valid request or contact to customer center");
  }
}
