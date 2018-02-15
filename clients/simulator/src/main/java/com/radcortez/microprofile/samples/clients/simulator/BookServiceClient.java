package com.radcortez.microprofile.samples.clients.simulator;

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
        final URL bookServiceUrl = new URL("http://localhost:8080/book-api");
        final BookService bookService =
                RestClientBuilder.newBuilder()
                                 .baseUrl(bookServiceUrl)
                                 .build(BookService.class);

        final JsonObject book = Json.createObjectBuilder()
                                    .add("author", "Roberto Cortez")
                                    .add("title", "Awesome Book")
                                    .add("year", 2018)
                                    .add("genre", "Tech")
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
