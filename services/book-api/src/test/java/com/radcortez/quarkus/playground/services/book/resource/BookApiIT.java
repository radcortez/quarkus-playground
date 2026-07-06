package com.radcortez.quarkus.playground.services.book.resource;

import com.radcortez.quarkus.playground.services.book.auth.TokenUtils;
import com.radcortez.quarkus.playground.services.book.model.BookCreate;
import com.radcortez.quarkus.playground.services.book.model.BookRead;
import com.radcortez.quarkus.playground.services.book.model.BookUpdate;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusIntegrationTest
@WithTestResource(value = NumberApiResource.class)
class BookApiIT {
    @Test
    void get() {
        BookCreate bookCreate =
                BookCreate.builder()
                        .title("Dragon Ball")
                        .author("Akira Torayama")
                        .genre("Manga")
                        .publishYear(1984)
                        .build();

        BookRead book = given()
                .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
                .header("Content-Type", APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .body(bookCreate)
                .post("/books")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract().as(BookRead.class);

        given()
            .header("Content-Type", APPLICATION_JSON)
            .header("Accept", APPLICATION_JSON)
            .get("/books/{id}", book.getId())
            .then()
            .statusCode(OK.getStatusCode());
    }

    @Test
    void getAll() {
        given()
            .header("Content-Type", APPLICATION_JSON)
            .header("Accept", APPLICATION_JSON)
            .get("/books")
            .then()
            .statusCode(OK.getStatusCode())
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void create() {
        BookCreate bookCreate =
            BookCreate.builder()
                      .title("Dragon Ball")
                      .author("Akira Torayama")
                      .genre("Manga")
                      .publishYear(1984)
                      .build();

        BookRead book = given()
                .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
                .header("Content-Type", APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .body(bookCreate)
                .post("/books")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract().as(BookRead.class);

        assertNotNull(book.getId());
        assertNotNull(book.getIsbn());
    }

    @Test
    void update() {
        BookCreate bookCreate =
                BookCreate.builder()
                        .title("Dragon Ball")
                        .author("Akira Torayama")
                        .genre("Manga")
                        .publishYear(1984)
                        .build();

        BookRead book = given()
                .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
                .header("Content-Type", APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .body(bookCreate)
                .post("/books")
                .then()
                .statusCode(CREATED.getStatusCode())
                .extract().as(BookRead.class);

        BookUpdate bookUpdate = book.toBookUpdate().toBuilder().publishYear(2000).build();
        BookRead bookRead = given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .header("Content-Type", APPLICATION_JSON)
            .header("Accept", APPLICATION_JSON)
            .body(bookUpdate)
            .put("/books/{id}", book.getId())
            .then()
            .statusCode(OK.getStatusCode())
            .extract().as(BookRead.class);
        assertEquals(2000, bookRead.getPublishYear());
    }

    @Test
    void delete() {
        BookCreate bookCreate =
            BookCreate.builder()
                      .title("Dragon Ball")
                      .author("Akira Torayama")
                      .genre("Manga")
                      .publishYear(1984)
                      .build();

        BookRead createdBook = given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .header("Content-Type", APPLICATION_JSON)
            .header("Accept", APPLICATION_JSON)
            .body(bookCreate)
            .post("/books")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(BookRead.class);

        assertNotNull(createdBook.getId());

        given()
            .header("Authorization", "Bearer " + TokenUtils.generateTokenString("admin", "admin"))
            .header("Content-Type", APPLICATION_JSON)
            .header("Accept", APPLICATION_JSON)
            .delete("/books/{id}", createdBook.getId())
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }
}
