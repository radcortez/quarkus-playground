package com.radcortez.quarkus.playground.services.book.persistence;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.quarkus.playground.services.book.FlywayTestDataSourceProvider;
import com.radcortez.quarkus.playground.services.book.client.IsbnGenerator;
import com.radcortez.quarkus.playground.services.book.entity.Book;
import com.radcortez.quarkus.playground.services.book.persistence.BookRepositoryTest.H2TestProfile;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(value = H2DatabaseTestResource.class, restrictToAnnotatedClass = true)
@TestProfile(H2TestProfile.class)
@FlywayTest(@DataSource(FlywayTestDataSourceProvider.class))
class BookRepositoryTest {
    @Inject
    BookRepository bookRepository;
    @InjectSpy
    IsbnGenerator isbnGenerator;

    @Test
    void find() {
        assertTrue(bookRepository.find(-1L).isPresent());
    }

    @Test
    void findAll() {
        assertFalse(bookRepository.find().isEmpty());
    }

    @Test
    void create() {
        Optional<Book> book =
            bookRepository.create(Book.builder()
                                      .author("Masashi Kishimoto")
                                      .title("Naruto")
                                      .genre("Manga")
                                      .publishYear(1997)
                                      .build());
        assertTrue(book.isPresent());
        assertTrue(bookRepository.find(book.get().getId()).isPresent());
        assertNotNull(book.get().getIsbn());
        verify(isbnGenerator).generateIsbn(book.get());
    }

    @Test
    void update() {
        Optional<Book> book =
            bookRepository.create(Book.builder()
                                      .author("")
                                      .title("Uzumaki Naruto")
                                      .genre("Manga")
                                      .build());
        assertTrue(book.isPresent());

        Book bookUpdate = book.get();
        bookUpdate.setPublishYear(1997);

        Optional<Book> updated = bookRepository.update(bookUpdate.getId(), bookUpdate);
        assertTrue(updated.isPresent());
        Book bookUpdated = updated.get();
        assertEquals(1997, bookUpdated.getPublishYear());
    }

    @Test
    void delete() {
    }

    @Test
    void countWithoutIsbn() {
    }

    public static class H2TestProfile implements QuarkusTestProfile {
        @Override
        public String getConfigProfile() {
            return "test,h2";
        }
    }
}
