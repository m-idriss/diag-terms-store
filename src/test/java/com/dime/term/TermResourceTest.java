package com.dime.term;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class TermResourceTest {

  private static final String TERMS_ENDPOINT = "api/v1/terms";
  private static final String DOMAIN_PATTERN = "http://[^/]+/";
  private static final String TERM_HEALTH = "{\"word\":\"test-health\",\"synonyms\":[\"wellness\"]}";
  private static final String TERM_TRY = "{\"word\":\"test-TRY\",\"synonyms\":[\"testtry\",\"testtry1\"]}";
  private static final String TERM_TEST = "{\"word\":\"test-test\",\"synonyms\":[\"testrun\"]}";

  private final List<Integer> createdTermIds = new ArrayList<>();
  private int healthId;

  @BeforeEach
  public void setup() {
    healthId = createTerm(TERM_HEALTH);
  }

  @AfterEach
  public void tearDown() {
    for (int id : createdTermIds) {
      deleteTermById(id);
    }
    createdTermIds.clear();
  }

  private int createTerm(String termPayload) {
    int id = given()
        .body(termPayload)
        .contentType(ContentType.JSON)
        .post(TERMS_ENDPOINT)
        .then()
        .statusCode(201)
        .extract()
        .path("id");
    createdTermIds.add(id);
    return id;
  }

  private void deleteTermById(int id) {
    given()
        .delete(TERMS_ENDPOINT + "/" + id);
  }

  private void deleteTermByWord(String word) {
    given()
        .delete(TERMS_ENDPOINT + "/" + word);
  }

  @Test
  void testPostTerm() {
    int termId = createTerm("{\"word\":\"test-terms\",\"synonyms\":[\"run\"]}");
    given()
        .get(TERMS_ENDPOINT + "/" + termId)
        .then()
        .statusCode(200)
        .body("word", equalTo("test-terms"))
        .body("synonyms", hasItem("run"));
  }


  @Test
  void testGetTermById() {
    given()
        .get(TERMS_ENDPOINT + "/" + healthId)
        .then()
        .statusCode(200)
        .body("id", equalTo(healthId))
        .body("word", equalTo("test-health"))
        .body("synonyms", hasItem("wellness"));
  }

  @Test
  void testGetTermByIdNotFound() {

    given()
        .when().get(TERMS_ENDPOINT + "/0")
        .then()
        .statusCode(404)
        .body("status", equalTo(404))
        .body("message", equalTo("Term with id [0] not found"))
        .body("errorKey", equalTo("ID_NOT_FOUND"))
        .body("path", matchesPattern(DOMAIN_PATTERN + TERMS_ENDPOINT + "/0"));
  }

  @Test
  void testGetTermByWordNotFound() {

    given()
        .when().get(TERMS_ENDPOINT + "/fofo")
        .then()
        .statusCode(404)
        .body("status", equalTo(404))
        .body("message", equalTo("Term with word [fofo] do not exist"))
        .body("errorKey", equalTo("WORD_NOT_FOUND"))
        .body("path", matchesPattern(DOMAIN_PATTERN + TERMS_ENDPOINT + "/fofo"));
  }

  @Test
  void testGetTermByWord() {

    given()
        .when().get(TERMS_ENDPOINT + "/test-health")
        .then()
        .statusCode(200)
        .body("id", equalTo(healthId))
        .body("word", equalTo("test-health"))
        .body("synonyms", hasSize(1))
        .body("synonyms", hasItem("wellness"));
  }

  @Test
  void testListAllTerms() {
    int tryId = createTerm(TERM_TRY);
    int testId = createTerm(TERM_TEST);

    given()
        .get(TERMS_ENDPOINT)
        .then()
        .statusCode(200)
        .body("size()", equalTo(3))
        .body("id", hasItems(healthId, tryId, testId))
        .body("word", hasItems("test-health", "test-try", "test-test"))
        .body("synonyms.flatten()", hasItems("wellness", "testtry", "testtry1", "testrun"));
  }

  @Test
  void testDeleteByWord() {
    deleteTermByWord("test-health");
    given()
        .get(TERMS_ENDPOINT + "/" + healthId)
        .then()
        .statusCode(404);
  }

  @Test
  void testDeleteByWordNotFound() {
    deleteTermByWord("non-existent");
    given()
        .get(TERMS_ENDPOINT + "/non-existent")
        .then()
        .statusCode(404);
  }
}
