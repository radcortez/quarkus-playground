package com.radcortez.quarkus.playground.services.book.persistence;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class BookRepositoryTest {
    @Inject
    BookRepository bookRepository;

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
        final Optional<Book> book =
            bookRepository.create(Book.builder()
                                      .author("Masashi Kishimoto")
                                      .title("Naruto")
                                      .genre("Manga")
                                      .publishYear(1997)
                                      .build());
        assertTrue(book.isPresent());
        assertTrue(bookRepository.find(book.get().getId()).isPresent());
    }

    @Test
    void update() {
        final Optional<Book> book =
            bookRepository.create(Book.builder()
                                      .author("")
                                      .title("Uzumaki Naruto")
                                      .genre("Manga")
                                      .build());
        assertTrue(book.isPresent());

        final Book bookUpdate = book.get();
        bookUpdate.setPublishYear(1997);

        final Optional<Book> updated = bookRepository.update(bookUpdate.getId(), bookUpdate);
        assertTrue(updated.isPresent());
        final Book bookUpdated = updated.get();
        assertEquals(1997, bookUpdated.getPublishYear());
    }

    @Test
    void delete() {
    }

    @Test
    void countWithoutIsbn() {
    }
}
