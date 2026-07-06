package com.radcortez.quarkus.playground.services.book.resource;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.quarkus.playground.services.book.FlywayTestDataSourceProvider;
import com.radcortez.quarkus.playground.services.book.model.BookCreate;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.HttpHeaders.ACCEPT;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;

@QuarkusTest
@FlywayTest(@DataSource(FlywayTestDataSourceProvider.class))
@TestHTTPEndpoint(BookResource.class)
class BookResourceTest {
    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(
                (requestSpec, responseSpec, ctx) -> {
                    requestSpec.header(new Header(CONTENT_TYPE, APPLICATION_JSON));
                    requestSpec.header(new Header(ACCEPT, APPLICATION_JSON));
                    return ctx.next(requestSpec, responseSpec);
                },
                new RequestLoggingFilter(),
                new ResponseLoggingFilter());
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }

    @Test
    @TestSecurity(user = "admin", roles = "admin")
    @JwtSecurity
    void create() {
        BookCreate bookCreate =
                BookCreate.builder()
                        .title("Dragon Ball")
                        .author("Akira Torayama")
                        .genre("Manga")
                        .publishYear(1984)
                        .build();

        given()
                .body(bookCreate)
                .post()
                .then()
                .statusCode(CREATED.getStatusCode());
    }
}
