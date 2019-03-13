package com.microprofile.samples.services.book.persistence;

import com.microprofile.samples.services.book.entity.Book;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(SUPPORTS)
public class BookBean {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Book> findById(final Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    public List<Book> findAll() {
        return entityManager.createQuery("SELECT m FROM Book m", Book.class).getResultList();
    }

    @Transactional(REQUIRED)
    public Book create(final Book book) {
        entityManager.persist(book);
        return book;
    }

    @Transactional(REQUIRED)
    public Book update(final Book book) {
        return entityManager.merge(book);
    }

    @Transactional(REQUIRED)
    public void deleteById(final Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
