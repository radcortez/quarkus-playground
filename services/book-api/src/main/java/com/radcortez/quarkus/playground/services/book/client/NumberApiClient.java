package com.radcortez.quarkus.playground.services.book.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterRestClient(configKey = "number-api")
public interface NumberApiClient {
    @GET
    @Path("/numbers/generate")
    @Produces(MediaType.TEXT_PLAIN)
    String generateNumber();

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    Response health();
}
