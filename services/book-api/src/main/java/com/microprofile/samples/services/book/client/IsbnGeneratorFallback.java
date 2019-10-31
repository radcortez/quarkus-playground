package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class IsbnGeneratorFallback implements FallbackHandler<Book> {
    @Inject
    @Channel("isbn-fallback")
    Emitter<Message<Book>> emitter;

    @Override
    public Book handle(final ExecutionContext executionContext) {
        final Book book = (Book) executionContext.getParameters()[0];
        book.setIsbn("ISBN-FALLBACK");
        emitter.send(Message.of(book));
        return book;
    }
}
