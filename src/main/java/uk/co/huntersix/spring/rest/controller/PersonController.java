package uk.co.huntersix.spring.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.dto.PersonRequestDto;
import uk.co.huntersix.spring.rest.dto.PersonResponseDto;
import uk.co.huntersix.spring.rest.service.PersonService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class PersonController {
  Logger logger = LoggerFactory.getLogger(PersonController.class);
  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/person/{lastName}/{firstName}")
  public ResponseEntity<PersonResponseDto> person(
      @PathVariable(value = "lastName") String lastName,
      @PathVariable(value = "firstName") String firstName) {
    logger.info("get person; firstName: {}, lastName: {}", firstName, lastName);
    PersonResponseDto personResponseDto = personService.findPerson(lastName, firstName);
    return ResponseEntity.ok(personResponseDto);
  }

  @GetMapping("/person/{lastName}")
  public ResponseEntity<List<PersonResponseDto>> searchPersonWithSurname(
      @PathVariable(value = "lastName") @NotNull String lastName) {
    logger.info("search person with lastname; lastName: {}", lastName);
    List<PersonResponseDto> people = personService.findPeopleWithSurname(lastName);
    return ResponseEntity.ok(people);
  }

  @PostMapping("/person")
  public ResponseEntity<PersonResponseDto> createPerson(
      @Valid @RequestBody PersonRequestDto personRequestDto) {
    logger.info(
        "create an unique Person; firstName: {}, lastName: {}",
        personRequestDto.getFirstName(),
        personRequestDto.getLastName());
    PersonResponseDto personResponseDto = personService.createPerson(personRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(personResponseDto);
  }
}
