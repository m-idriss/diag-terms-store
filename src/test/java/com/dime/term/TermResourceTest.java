package com.dime.term;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
class TermResourceTest {

    @Test
    void testSendTerm() {
        Term term = new Term();
        term.word = "The Godfather";
        term.description = "I'm gonna make him an offer he can't refuse.";
        String body = given()
                .contentType("application/json")
                .body(term)
                .when().post("/v1/terms")
                .then()
                .statusCode(201).extract().asString();

        assertTrue(body.contains("The Godfather"));
        assertTrue(body.contains("I'm gonna make him an offer he can't refuse."));
    }

}