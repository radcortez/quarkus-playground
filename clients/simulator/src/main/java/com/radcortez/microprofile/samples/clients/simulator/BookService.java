package com.radcortez.microprofile.samples.clients.simulator;


import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public interface BookService {
    @GET
    JsonArray findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") final Long id);

    @POST
    Response create(final JsonObject book);
}
