package com.microprofile.samples.clients.standalone;

import com.github.javafaker.Faker;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

public class BookServiceClient {
    public static void main(String[] args) throws MalformedURLException {

        final Faker faker = new Faker();

        final String bookServiceTargetUrl =
                ConfigProvider.getConfig()
                              .getOptionalValue("BOOK_TARGET_URL", String.class)
                              .orElse("http://localhost:8080/book-api");

        final BookService bookService =
                RestClientBuilder.newBuilder()
                                 .baseUrl(new URL(bookServiceTargetUrl))
                                 .build(BookService.class);

        final JsonObject book = Json.createObjectBuilder()
                                    .add("author", faker.book().author())
                                    .add("title", faker.book().title())
                                    .add("year", faker.number().numberBetween(1900, 2019))
                                    .add("genre", faker.book().genre())
                                    .build();

        final Response response = bookService.create(book);
        if (CREATED.getStatusCode() == response.getStatus()) {
            final String id = response.getLocation().getPath()
                                      .substring(response.getLocation().getPath().lastIndexOf("/") + 1);

            final Response bookResponse = bookService.findById(Long.valueOf(id));
            if (OK.getStatusCode() == bookResponse.getStatus()) {
                final JsonObject bookResult = bookResponse.readEntity(JsonObject.class);
                System.out.println("bookResult = " + bookResult);
            }
        }
    }
}
