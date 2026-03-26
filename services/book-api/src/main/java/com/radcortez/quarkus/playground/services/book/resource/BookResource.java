package com.radcortez.quarkus.playground.services.book.resource;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import com.radcortez.quarkus.playground.services.book.model.BookCreate;
import com.radcortez.quarkus.playground.services.book.model.BookRead;
import com.radcortez.quarkus.playground.services.book.model.BookUpdate;
import com.radcortez.quarkus.playground.services.book.persistence.BookRepository;
import com.radcortez.quarkus.playground.services.book.tracer.TraceLog;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.created;

@ApplicationScoped
public class BookResource implements BookApi {
    @Context
    UriInfo uriInfo;
    @Inject
    BookRepository bookRepository;

    @Override
    public Response get(final Long id) {
        return bookRepository.find(id)
                             .map(Book::toBookRead)
                             .map(Response::ok)
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }

    @Override
    public Response get() {
        final List<BookRead> books =
            bookRepository.find()
                          .stream()
                          .map(Book::toBookRead)
                          .collect(toList());
        return Response.ok(books).build();
    }

    @Override
    @RolesAllowed("admin")
    @TraceLog
    public Response create(final BookCreate bookCreate) {
        return bookRepository.create(bookCreate.toBook())
                             .map(Book::toBookRead)
                             .map(book -> created(
                                 uriInfo.getRequestUriBuilder().path("{id}").build(book.getId())).entity(book))
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }

    @Override
    @RolesAllowed("admin")
    @TraceLog
    public Response update(final Long id, final BookUpdate bookUpdate) {
        return bookRepository.update(id, bookUpdate.toBook())
                             .map(Book::toBookRead)
                             .map(Response::ok)
                             .orElse(Response.status(NOT_FOUND))
                             .build();

    }

    @Override
    @RolesAllowed("admin")
    @TraceLog
    public Response delete(final Long id) {
        return bookRepository.delete(id)
                             .map(book -> Response.noContent())
                             .orElse(Response.status(NOT_FOUND))
                             .build();
    }

    public Long countWithoutIsbn() {
        return bookRepository.countWithoutIsbn();
    }
}
