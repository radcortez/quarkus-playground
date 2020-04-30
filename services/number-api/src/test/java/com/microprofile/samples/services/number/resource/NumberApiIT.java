package com.microprofile.samples.services.number.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class NumberApiIT {
    @Test
    void name() {
        given().get("/numbers/generate").then().statusCode(200).body(Matchers.startsWith("BK"));
    }
}
