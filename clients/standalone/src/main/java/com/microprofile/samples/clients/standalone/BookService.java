package com.microprofile.samples.clients.standalone;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
