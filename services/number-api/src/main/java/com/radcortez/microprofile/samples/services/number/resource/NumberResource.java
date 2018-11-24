package com.radcortez.microprofile.samples.services.number.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("numbers")
@Produces(MediaType.TEXT_PLAIN)
public class NumberResource {

    @GET
    @Path("generate")
    public Response generate() {
        return Response.ok(Math.random()).build();
    }
}
