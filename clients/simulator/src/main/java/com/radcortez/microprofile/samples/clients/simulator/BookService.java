package com.radcortez.microprofile.samples.clients.simulator;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.decorator.Decorator;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
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

// Decorator annotation is used as a workaround for TomEE CdiScanner to recogzine this as a CDI Bean.
@Decorator
@RegisterRestClient
@Path("/books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
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
