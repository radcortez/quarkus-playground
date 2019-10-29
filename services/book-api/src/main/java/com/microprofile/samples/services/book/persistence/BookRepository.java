package com.microprofile.samples.services.book.persistence;

import com.microprofile.samples.services.book.client.NumberApiClient;
import com.microprofile.samples.services.book.entity.Book;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional
public class BookRepository {
    @Inject
    private EntityManager entityManager;
    @Inject
    @RestClient
    private NumberApiClient numberApiClient;

    public Optional<Book> find(final Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    public List<Book> find() {
        return entityManager.createQuery("SELECT m FROM Book m", Book.class).getResultList();
    }

    public Optional<Book> create(final Book book) {
        book.setIsbn(numberApiClient.generateNumber());
        entityManager.persist(book);
        return Optional.of(book);
    }

    public Optional<Book> update(final Long id, final Book bookUpdate) {
        return find(id).map(book -> book.toBook(bookUpdate));
    }

    public Optional<Book> delete(final Long id) {
        return find(id).map(book -> {
            entityManager.remove(book);
            return book;
        });
    }
}
