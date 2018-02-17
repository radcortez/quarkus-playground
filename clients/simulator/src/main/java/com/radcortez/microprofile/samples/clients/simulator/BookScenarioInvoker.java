package com.radcortez.microprofile.samples.clients.simulator;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.unmodifiableList;

@ApplicationScoped
public class BookScenarioInvoker extends ScenarioInvoker {
    @Inject
    @RestClient
    private BookService bookService;

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
                   .add("author", "Roberto Cortez")
                   .add("title", "Awesome Book")
                   .add("year", 2018)
                   .add("genre", "Tech")
                   .build();
    }

    private Long getRandomBook() {
        final JsonArray all = bookService.findAll().readEntity(JsonArray.class);
        final JsonObject object = all.getJsonObject((int) (Math.random() * all.size()));
        return object.getJsonNumber("id").longValue();
    }
}
