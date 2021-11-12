package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
  private static final List<Person> PERSON_DATA =
      new ArrayList<>(
          Arrays.asList(
              new Person("Mary", "Smith"),
              new Person("Brian", "Archer"),
              new Person("Larry", "Archer"),
              new Person("Collin", "Brown")));

  public Optional<Person> findPersonByLastNameAndFirstName(String lastName, String firstName) {
    return PERSON_DATA.stream()
        .filter(
            p ->
                p.getFirstName().equalsIgnoreCase(firstName)
                    && p.getLastName().equalsIgnoreCase(lastName))
        .findFirst();
  }

  public List<Person> findPersonBySurname(String lastName) {
    return PERSON_DATA.stream()
        .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
        .collect(Collectors.toList());
  }

  public void save(Person person) {
    PERSON_DATA.add(person);
  }
}
