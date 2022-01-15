package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
import com.microprofile.samples.services.book.persistence.BookRepository;
import io.opentelemetry.extension.annotations.WithSpan;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IsbnGenerator {
    @Inject
    @RestClient
    NumberApiClient numberApiClient;

    @Fallback(IsbnGeneratorFallback.class)
    public Book generateIsbn(final Book book) {
        book.setIsbn(numberApiClient.generateNumber());
        return book;
    }

    @Inject
    BookRepository bookRepository;

    @WithSpan
    public void fallbackIsbn(final Book book) {
        book.setIsbn("ISBN" + "-" + (int) Math.floor((Math.random() * 9999999)) + 1);
        bookRepository.update(book.getId(), book);
    }
}
