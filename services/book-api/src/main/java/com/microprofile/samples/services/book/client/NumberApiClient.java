package com.microprofile.samples.services.book.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@RegisterRestClient
@Path("/numbers")
@Produces(MediaType.TEXT_PLAIN)
public interface NumberApiClient {
    @GET
    @Path("/generate")
    String generateNumber();
}
