package com.radcortez.quarkus.playground.clients.simulator;

import net.datafaker.Faker;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.unmodifiableList;

@ApplicationScoped
public class BookScenarioInvoker extends ScenarioInvoker {
    @Inject
    @RestClient
    BookService bookService;

    @Inject
    Faker faker;

    @Override
    protected List<Supplier<Response>> getEndpoints() {
        final List<Supplier<Response>> endpoints = new ArrayList<>();
        endpoints.add(() -> bookService.findAll());
        endpoints.add(() -> bookService.findAll());
        endpoints.add(() -> bookService.findAll());
        endpoints.add(() -> bookService.findById(getRandomBook()));
        endpoints.add(() -> bookService.findById(getRandomBook()));
        endpoints.add(() -> bookService.findById(getRandomBook()));
        endpoints.add(() -> bookService.create(createBook()));
        endpoints.add(() -> bookService.create(createBook()));
        endpoints.add(() -> bookService.delete(getRandomBook()));
        return unmodifiableList(endpoints);
    }

    private JsonObject createBook() {
        return Json.createObjectBuilder()
                   .add("author", faker.book().author())
                   .add("title", faker.book().title())
                   .add("year", faker.number().numberBetween(1900, 2017))
                   .add("genre", faker.book().genre())
                   .build();
    }

    private Long getRandomBook() {
        final JsonArray all = bookService.findAll().readEntity(JsonArray.class);
        if (!all.isEmpty()) {
            final JsonObject object = all.getJsonObject((int) (Math.random() * all.size()));
            return object.getJsonNumber("id").longValue();
        } else {
            return 0L;
        }
    }
}
