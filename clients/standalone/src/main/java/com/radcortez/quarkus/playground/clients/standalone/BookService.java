package com.radcortez.quarkus.playground.clients.standalone;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@RegisterProvider(value = OAuthAuthenticator.class, priority = Priorities.AUTHENTICATION + 100) // TODO - radcortez - Bug in Resteasy and priority with @RegisterProvider
// org/jboss/resteasy/core/providerfactory/ClientHelper.java:185
// org/jboss/resteasy/microprofile/client/RestClientBuilderImpl.java:187
public interface BookService {
    @GET
    Response findAll();

    @GET
    @Path("/{id}")
    Response findById(@PathParam("id") final Long id);

    @POST
    Response create(final Book book);

    @DELETE
    @Path("/{id}")
    Response delete(@PathParam("id") final Long id);
}
