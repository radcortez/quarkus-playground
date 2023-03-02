package com.radcortez.quarkus.playground.clients.simulator;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RegisterRestClient
@Path("/books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@RegisterProvider(OAuthCredentials.class)
@RegisterProvider(OAuthAuthenticator.class)
public interface BookService {
    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") final Long id);

    @POST
    Response create(final JsonObject book);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") final Long id);
}
