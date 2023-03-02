package com.radcortez.quarkus.playground.services.book.client;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import io.quarkus.opentelemetry.runtime.QuarkusContextStorage;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.TracingMetadata;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class IsbnFallback {
    @Inject
    ManagedExecutor executor;
    @Inject
    IsbnGenerator isbnGenerator;

    @Incoming("books-fallback")
    public Uni<Void> fallback(final Message<Book> message) {
        return Uni.createFrom().item(message.getPayload())
                  .onItem().delayIt().by(Duration.ofSeconds(5))
                  .emitOn(executor)
                  .onItem().invoke(book -> {
                      message.getMetadata(TracingMetadata.class)
                             .ifPresent(metadata -> QuarkusContextStorage.INSTANCE.attach(metadata.getCurrentContext()));
                      isbnGenerator.fallbackIsbn(book);
                  })
                  .onItem().transformToUni(m -> Uni.createFrom().completionStage(message.ack()));
    }
}
