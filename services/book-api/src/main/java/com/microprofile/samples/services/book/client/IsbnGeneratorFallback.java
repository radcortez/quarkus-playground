package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import javax.enterprise.context.Dependent;

@Dependent
public class IsbnGeneratorFallback implements FallbackHandler<Book> {
    @Override
    public Book handle(final ExecutionContext executionContext) {
        final Book book = (Book) executionContext.getParameters()[0];
        book.setIsbn("ISBN-FALLBACK");
        return book;
    }
}
