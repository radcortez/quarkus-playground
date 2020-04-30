package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
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
}
