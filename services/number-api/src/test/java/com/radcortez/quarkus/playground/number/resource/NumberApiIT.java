package com.radcortez.quarkus.playground.number.resource;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
class NumberApiIT {
    @Test
    void name() {
        given().get("/numbers/generate").then().statusCode(200).body(Matchers.startsWith("BK"));
    }
}
