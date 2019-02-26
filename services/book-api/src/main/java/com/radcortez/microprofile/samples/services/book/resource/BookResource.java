package com.radcortez.microprofile.samples.services.book.resource;

import com.radcortez.microprofile.samples.services.book.entity.Book;
import com.radcortez.microprofile.samples.services.book.persistence.BookBean;
import com.radcortez.microprofile.samples.services.book.service.NumberService;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@ApplicationScoped
@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Traced
public class BookResource {
    @Inject
    private BookBean bookBean;

    @Inject
    private NumberService numberService;

    @GET
    @Path("/{id}")
    @Metered(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.findById_meter")
    @Timed(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.findById_timer")
    public Response findById(@PathParam("id") final Long id) {
        return bookBean.findById(id)
                .map(Response::ok)
                .orElse(status(NOT_FOUND))
                .build();
    }

    @GET
    @Metered(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.findAll_meter")
    @Timed(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.findAll_timer")
    public Response findAll() {
        return ok(bookBean.findAll()).build();
    }

    @POST
    @Metered(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.create_meter")
    @Timed(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.create_timer")
    public Response create(final Book book, @Context UriInfo uriInfo) {
        final String number = numberService.getNumber();
        book.setIsbn(number);

        final Book created = bookBean.create(book);
        final URI createdURI = uriInfo.getBaseUriBuilder()
                .path("books/{id}")
                .resolveTemplate("id", created.getId())
                .build();
        return Response.created(createdURI).build();
    }

    @PUT
    @Metered(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.update_meter")
    @Timed(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.update_timer")
    public Response update(final Book book) {
        return ok(bookBean.update(book)).build();
    }

    @DELETE
    @Path("/{id}")
    @Metered(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.delete_meter")
    @Timed(name = "com.radcortez.microprofile.samples.services.book.resource.BookResource.delete_timer")
    public Response delete(@PathParam("id") final Long id) {
        bookBean.deleteById(id);
        return noContent().build();
    }
}
