package uk.co.huntersix.spring.rest.dto;

import uk.co.huntersix.spring.rest.model.Person;

public final class PersonResponseDto {
  public final String firstName;
  public final String lastName;

  public PersonResponseDto(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public static PersonResponseDto from(Person person) {
    return new PersonResponseDto(person.getFirstName(), person.getLastName());
  }
}
