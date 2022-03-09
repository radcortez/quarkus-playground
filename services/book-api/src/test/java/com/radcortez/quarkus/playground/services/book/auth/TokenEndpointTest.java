package com.radcortez.quarkus.playground.services.book.auth;

import com.radcortez.quarkus.playground.services.book.resource.JsonbObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipalFactory;
import io.smallrye.jwt.config.JWTAuthContextInfoProvider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class TokenEndpointTest {
    @BeforeAll
    static void beforeAll() {
        RestAssured.config = RestAssured.config().objectMapperConfig(new ObjectMapperConfig(new JsonbObjectMapper()));
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }

    @Inject
    JWTAuthContextInfoProvider jwtAuthContextInfoProvider;

    @Test
    void token() throws Exception {
        final Response response =
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

        final JsonWebToken jwt =
            JWTCallerPrincipalFactory.instance().parse(response.jsonPath().get("access_token"),
                                                       jwtAuthContextInfoProvider.getContextInfo());

        assertEquals("naruto", jwt.getName());
    }
}
