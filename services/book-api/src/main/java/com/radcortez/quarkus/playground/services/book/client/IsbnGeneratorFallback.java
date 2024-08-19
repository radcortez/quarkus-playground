package com.radcortez.quarkus.playground.services.book.client;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.reactive.messaging.TracingMetadata;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class IsbnGeneratorFallback implements FallbackHandler<Book> {
    @Inject
    @Channel("fallback")
    Emitter<Book> emitter;

    @Override
    @WithSpan
    public Book handle(final ExecutionContext executionContext) {
        Book book = (Book) executionContext.getParameters()[0];
        book.setIsbn("ISBN-FALLBACK");
        emitter.send(Message.of(book, Metadata.of(TracingMetadata.withCurrent(Context.current()))));
        return book;
    }
}
