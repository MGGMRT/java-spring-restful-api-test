package uk.co.huntersix.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.dto.PersonRequestDto;
import uk.co.huntersix.spring.rest.dto.PersonResponseDto;
import uk.co.huntersix.spring.rest.exception.PersonConflictException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.exception.PersonSearchContentNotFound;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
  private final PersonDataService personDataService;

  @Autowired
  public PersonService(PersonDataService personDataService) {
    this.personDataService = personDataService;
  }

  public PersonResponseDto findPerson(String lastName, String firstName) {
    Person person =
        personDataService
            .findPersonByLastNameAndFirstName(lastName, firstName)
            .orElseThrow(
                () ->
                    new PersonNotFoundException(
                        "Person not found; firstName: " + firstName + " lastName: " + lastName));
    return PersonResponseDto.from(person);
  }

  public List<PersonResponseDto> findPeopleWithSurname(String lastName) {
    List<Person> personListBySurname = personDataService.findPersonBySurname(lastName);
    if (personListBySurname.isEmpty()) {
      throw new PersonSearchContentNotFound("People Not Found");
    }
    return convertResponseDto(personListBySurname);
  }

  private List<PersonResponseDto> convertResponseDto(List<Person> personList) {
    return personList.stream().map(PersonResponseDto::from).collect(Collectors.toList());
  }

  public PersonResponseDto createPerson(PersonRequestDto personRequestDto) {
    Optional<Person> personOpt =
        personDataService.findPersonByLastNameAndFirstName(
            personRequestDto.getLastName(), personRequestDto.getFirstName());

    if (personOpt.isPresent()) {
      throw new PersonConflictException(
          "The Person already exists; firstName: "
              + personRequestDto.getFirstName()
              + " - lastName: "
              + personRequestDto.getLastName());
    }
    Person newPerson = new Person(personRequestDto.getFirstName(), personRequestDto.getLastName());
    personDataService.save(newPerson);
    return PersonResponseDto.from(newPerson);
  }
}
