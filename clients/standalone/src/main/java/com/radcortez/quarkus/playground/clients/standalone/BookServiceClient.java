package com.radcortez.quarkus.playground.clients.standalone;

import com.github.javafaker.Faker;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;

import static com.radcortez.quarkus.playground.clients.standalone.OAuthCredentials.oauthCredentials;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

public class BookServiceClient {
    public static void main(String[] args) throws MalformedURLException {

        final Faker faker = new Faker();

        final String bookServiceTargetUrl =
                ConfigProvider.getConfig()
                              .getOptionalValue("BOOK_TARGET_URL", String.class)
                              .orElse("http://localhost:8080/");

        final BookService bookService =
                RestClientBuilder.newBuilder()
                                 .baseUrl(new URL(bookServiceTargetUrl))
                                 .register(oauthCredentials("naruto", "", "client_credentials", "admin"))
                                 .build(BookService.class);

        final Book book = Book.builder()
                .author(faker.book().author())
                .title(faker.book().title())
                .publishYear(faker.number().numberBetween(1900, 2019))
                .genre(faker.book().genre())
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
