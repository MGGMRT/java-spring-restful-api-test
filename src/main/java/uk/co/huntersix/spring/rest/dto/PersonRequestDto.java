package uk.co.huntersix.spring.rest.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PersonRequestDto {
  @NotNull @NotEmpty private String firstName;
  @NotNull @NotEmpty private String lastName;

  public PersonRequestDto() {}

  public PersonRequestDto(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
