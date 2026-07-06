package com.radcortez.quarkus.playground.services.book.auth;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class TokenEndpointTest {

    @Test
    void token() throws Exception {
        Response response =
            given()
                .auth().preemptive().basic("naruto", "naruto")
                .contentType(ContentType.URLENC)
                .log().all()
                .post("/oauth/token")
                .then()
                .log().all()
                .statusCode(OK.getStatusCode())
                .extract().response();

        assertNotNull(response.getBody().asString());
        assertNotNull(response.jsonPath().get("access_token"));
    }
}
