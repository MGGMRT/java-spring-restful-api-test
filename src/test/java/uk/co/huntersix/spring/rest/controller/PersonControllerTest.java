package uk.co.huntersix.spring.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.dto.PersonRequestDto;
import uk.co.huntersix.spring.rest.dto.PersonResponseDto;
import uk.co.huntersix.spring.rest.exception.PersonConflictException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.service.PersonService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PersonService personService;

  @Test
  public void shouldReturn200OKWhenPersonExists() throws Exception {

    // GIVEN
    PersonResponseDto personResponseDto = new PersonResponseDto("Mary", "Smith");
    when(personService.findPerson(anyString(), anyString())).thenReturn(personResponseDto);

    // WHEN THEN
    this.mockMvc
        .perform(get("/person/smith/mary"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Mary"))
        .andExpect(jsonPath("$.lastName").value("Smith"));
  }

  @Test()
  public void shouldReturn404PersonNotFoundExceptionWhenPersonNotExists() throws Exception {
    // GIVEN
    when(personService.findPerson(anyString(), anyString()))
        .thenThrow(PersonNotFoundException.class);

    // WHEN THEN
    this.mockMvc.perform(get("/person/steve/mary")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test()
  public void shouldReturnPeopleWhenPersonLastNameExists() throws Exception {

    // GIVEN
    ArrayList<PersonResponseDto> personResponseDtos =
        Lists.list(
            new PersonResponseDto("Alter", "Archer"), new PersonResponseDto("Brian", "Archer"));
    when(personService.findPeopleWithSurname(anyString())).thenReturn(personResponseDtos);

    // WHEN THEN
    this.mockMvc
        .perform(get("/person/archer"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("Alter"))
        .andExpect(jsonPath("$[0].lastName").value("Archer"))
        .andExpect(jsonPath("$[1].firstName").value("Brian"))
        .andExpect(jsonPath("$[1].lastName").value("Archer"));
  }

  @Test()
  public void shouldReturn404NotFoundWhenPersonLastNameNotExists() throws Exception {

    // GIVEN
    when(personService.findPeopleWithSurname(anyString())).thenThrow(PersonNotFoundException.class);

    // WHEN THEN
    this.mockMvc.perform(get("/person/archer")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test()
  public void shouldReturn200OKWhenNewPersonWithUniqueFirstNameOrLastName() throws Exception {

    // GIVEN
    PersonRequestDto personRequestDto = new PersonRequestDto("Ashley", "Spencer");
    PersonResponseDto personResponseDto = PersonResponseDto.from(new Person("Ashley", "Spencer"));
    when(personService.createPerson(personRequestDto)).thenReturn(personResponseDto);

    // WHEN THEN
    this.mockMvc
        .perform(
            post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(personRequestDto)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test()
  public void shouldReturn400BadRequestWhenPostRequestWithPersonRequestDtoHasNullValue()
      throws Exception {
    // GIVEN
    PersonRequestDto personRequestDto = new PersonRequestDto(null, "Archer");

    // WHEN THEN
    this.mockMvc
        .perform(
            post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(personRequestDto)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test()
  public void shouldReturn400BadRequestWhenPostRequestHasEmptyValue() throws Exception {
    // GIVEN
    PersonRequestDto personRequestDto = new PersonRequestDto("", "");

    // WHEN THEN
    this.mockMvc
        .perform(
            post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(personRequestDto)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
