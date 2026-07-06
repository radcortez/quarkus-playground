package com.radcortez.quarkus.playground.services.book.client;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusComponentTest
class IsbnGeneratorTest {
    @Inject
    IsbnGenerator isbnGenerator;
    @InjectMock
    @RestClient
    NumberApiClient numberApiClient;

    @Test
    void generateIsbn() {
        Mockito.when(numberApiClient.generateNumber()).thenReturn("BK-000000");
        Book book = isbnGenerator.generateIsbn(new Book());
        assertEquals("BK-000000", book.getIsbn());
    }

    @Test
    void fallbackIsbn() {
        Book book = isbnGenerator.generateIsbn(new Book());
        assertTrue(book.getIsbn().startsWith("ISBN"));
    }
}
