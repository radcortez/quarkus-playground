package com.radcortez.quarkus.playground.clients.simulator;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.QuarkusMainTest;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

@Disabled
@QuarkusMainTest
class InitializerTest {
    @Test
    @Launch
    void launch() {

    }

    @Alternative()
    @Priority(1)
    @ApplicationScoped
    @RestClient
    public static class BookServiceMock implements BookService {
        @Override
        public Response findAll() {
            return Response.ok(Json.createArrayBuilder().build()).build();
        }

        @Override
        public Response findById(Long id) {
            return Response.ok().build();
        }

        @Override
        public Response create(JsonObject book) {
            return Response.created(URI.create("http://localhost:8080")).entity(new Object()).build();
        }

        @Override
        public Response delete(Long id) {
            return Response.noContent().build();
        }
    }
}
