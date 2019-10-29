package com.microprofile.samples.services.book.resource;

import com.microprofile.samples.services.book.entity.Book;
import com.microprofile.samples.services.book.model.BookCreate;
import com.microprofile.samples.services.book.model.BookRead;
import com.microprofile.samples.services.book.model.BookUpdate;
import com.microprofile.samples.services.book.persistence.BookRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.created;

@ApplicationScoped
@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BookResource {
    @Context
    UriInfo uriInfo;
    @Inject
    BookRepository bookRepository;

    @GET
    @Path("/{id}")
    @Operation(summary = "Find a Book by Id")
    @APIResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Book.class))})
    public Response get(final Long id) {
        return bookRepository.find(id)
                             .map(Book::toBookRead)
                             .map(Response::ok)
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }


    @GET
    public Response get() {
        final List<BookRead> books =
            bookRepository.find()
                          .stream()
                          .map(Book::toBookRead)
                          .collect(toList());
        return Response.ok(books).build();
    }

    @POST
    public Response create(final BookCreate bookCreate) {
        return bookRepository.create(bookCreate.toBook())
                             .map(Book::toBookRead)
                             .map(book -> created(
                                 uriInfo.getRequestUriBuilder().path("{id}").build(book.getId())).entity(book))
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }

    @PUT
    public Response update(final Long id, final BookUpdate bookUpdate) {
        return bookRepository.update(id, bookUpdate.toBook())
                             .map(Book::toBookRead)
                             .map(Response::ok)
                             .orElse(Response.status(NOT_FOUND))
                             .build();

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") final Long id) {
        return bookRepository.delete(id)
                             .map(book -> Response.noContent())
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }
}
