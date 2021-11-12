package uk.co.huntersix.spring.rest.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.co.huntersix.spring.rest.dto.PersonResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest extends HttpIntegrationBaseTest {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void shouldReturnPersonWhenFirstNameAndLastNameAreExists() throws Exception {
    assertThat(
            this.restTemplate.getForObject(
                "http://localhost:" + port + "/person/smith/mary", String.class))
        .contains("Mary");
  }

  @Test
  public void shouldReturnNotFoundWhenPersonNotFoundGivenNameAndLastName() {
    ResponseEntity<String> response =
        restTemplate.getForEntity(getBaseURI() + "/person/smith/lee", String.class);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void shouldReturnPeopleWhenLastNameExists() {
    ResponseEntity<PersonResponseDto[]> resp =
        this.restTemplate.getForEntity(getBaseURI() + "/person/archer", PersonResponseDto[].class);
    Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(resp.getBody().length).isEqualTo(2);
    Assertions.assertThat(((PersonResponseDto) resp.getBody()[0]).lastName).isEqualTo("Archer");
    Assertions.assertThat(((PersonResponseDto) resp.getBody()[1]).lastName).isEqualTo("Archer");
  }

  @Test
  public void shouldReturnNoContentWhenLastNameNotExists() throws Exception {
    ResponseEntity<Object[]> resp =
        this.restTemplate.getForEntity(getBaseURI() + "/person/george", Object[].class);
    Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  public void shouldAddThePersonToTheListWhenGivenUniqueNameOrSurname() throws Exception {
    JSONObject jsonPersonRequestDto = new JSONObject();
    jsonPersonRequestDto.put("firstName", "Albert");
    jsonPersonRequestDto.put("lastName", "Smith");

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(getBaseURI() + "/person")
            .accept(MediaType.APPLICATION_JSON)
            .content(jsonPersonRequestDto.toString())
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = getMockMvc().perform(requestBuilder).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  public void shouldReturnConflictWhenPersonAlreadyExist() throws Exception {
    JSONObject jsonPersonRequestDto = new JSONObject();
    jsonPersonRequestDto.put("firstName", "Mary");
    jsonPersonRequestDto.put("lastName", "Smith");

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(getBaseURI() + "/person")
            .accept(MediaType.APPLICATION_JSON)
            .content(jsonPersonRequestDto.toString())
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = getMockMvc().perform(requestBuilder).andReturn();
    Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
  }

  private String getBaseURI() {
    return "http://localhost:" + port;
  }
}
