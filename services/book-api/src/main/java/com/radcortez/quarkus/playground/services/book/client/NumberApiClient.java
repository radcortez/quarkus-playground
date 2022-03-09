package com.radcortez.quarkus.playground.services.book.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
