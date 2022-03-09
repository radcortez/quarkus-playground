package com.radcortez.quarkus.playground.clients.simulator;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
