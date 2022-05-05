package com.radcortez.quarkus.playground.services.book.resource;

import com.radcortez.quarkus.playground.services.book.auth.TokenUtils;
import com.radcortez.quarkus.playground.services.book.model.BookCreate;
import com.radcortez.quarkus.playground.services.book.model.BookRead;
import com.radcortez.quarkus.playground.services.book.model.BookUpdate;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class BookApiIT {
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

        RestAssured.config = RestAssured.config().objectMapperConfig(new ObjectMapperConfig(new JsonbObjectMapper()));
    }

    @AfterAll
    static void afterAll() {
        RestAssured.reset();
    }

    @Test
    void get() {
        given()
            .get("/books/{id}", -1)
            .then()
            .statusCode(OK.getStatusCode());
    }

    @Test
    void getAll() {
        given()
            .get("/books")
            .then()
            .statusCode(OK.getStatusCode())
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void create() {
        final BookCreate bookCreate =
            BookCreate.builder()
                      .title("Dragon Ball")
                      .author("Akira Torayama")
                      .genre("Manga")
                      .publishYear(1984)
                      .build();

        given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .body(bookCreate)
            .post("/books")
            .then()
            .statusCode(CREATED.getStatusCode());
    }

    @Test
    void update() {
        final BookRead book = given()
            .get("/books/{id}", -1)
            .then()
            .statusCode(OK.getStatusCode())
            .extract().as(BookRead.class);

        final BookUpdate bookUpdate = book.toBookUpdate().toBuilder().publishYear(2000).build();
        final BookRead bookRead = given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .body(bookUpdate)
            .put("/books/{id}", -1)
            .then()
            .statusCode(OK.getStatusCode())
            .extract().as(BookRead.class);
        assertEquals(2000, bookRead.getPublishYear());
    }

    @Test
    void delete() {
        final BookCreate bookCreate =
            BookCreate.builder()
                      .title("Dragon Ball")
                      .author("Akira Torayama")
                      .genre("Manga")
                      .publishYear(1984)
                      .build();

        final BookRead createdBook = given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .body(bookCreate)
            .post("/books")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(BookRead.class);

        assertNotNull(createdBook.getId());

        given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .delete("/books/{id}", createdBook.getId())
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }
}
