package com.dime.health;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class HealthCheckTest {

    @Test
    public void getHealth() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("UP"));
    }

    @Test
    public void getDatasourceHealth() {
        given()
                .when().get("/health/ready")
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("UP"));
    }

}
