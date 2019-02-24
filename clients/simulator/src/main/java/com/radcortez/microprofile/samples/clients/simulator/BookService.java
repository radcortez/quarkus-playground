package com.radcortez.microprofile.samples.clients.simulator;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;

import javax.decorator.Decorator;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// Decorator annotation is used as a workaround for TomEE CdiScanner to recogzine this as a CDI Bean.
@Decorator
@RegisterRestClient
@RegisterProvider(BookService.TracingActivator.class)
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


    // workaround cause ClientTracingRegistrar requires to use ClientAPI and not @RegisterProvider
    @Dependent
    class TracingActivator implements ClientRequestFilter, ClientResponseFilter {
        private final ClientRequestFilter request;
        private final ClientResponseFilter response;

        public TracingActivator() {
            request = create("org.apache.geronimo.microprofile.opentracing.microprofile.client.CdiOpenTracingClientRequestFilter", ClientRequestFilter.class);
            response = create("org.apache.geronimo.microprofile.opentracing.microprofile.client.CdiOpenTracingClientResponseFilter", ClientResponseFilter.class);
        }

        @Override
        public void filter(final ClientRequestContext requestContext) throws IOException {
            if (request != null) {
                request.filter(requestContext);
            }
        }

        @Override
        public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) throws IOException {
            if (response != null) {
                response.filter(requestContext, responseContext);
            }
        }

        private static <T> T create(final String fnq, final Class<T> type) {
            try {
                return type.cast(CDI.current().select(Thread.currentThread().getContextClassLoader().loadClass(fnq)).get());
            } catch (final ClassNotFoundException e) {
                return null;
            }
        }
    }
}
