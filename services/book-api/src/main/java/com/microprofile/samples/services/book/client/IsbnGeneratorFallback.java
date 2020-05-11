package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class IsbnGeneratorFallback implements FallbackHandler<Book> {
    @Inject
    @Channel("books")
    Emitter<Book> emitter;

    @Override
    public Book handle(final ExecutionContext executionContext) {
        final Book book = (Book) executionContext.getParameters()[0];
        book.setIsbn("ISBN-FALLBACK");
        emitter.send(book);
        return book;
    }
}
