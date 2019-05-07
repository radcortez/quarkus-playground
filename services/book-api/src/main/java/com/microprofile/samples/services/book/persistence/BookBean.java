package com.microprofile.samples.services.book.persistence;

import com.microprofile.samples.services.book.entity.Book;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    @Inject
    private Tracer tracer;

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
        final Span activeSpan = tracer.activeSpan();
        final Tracer.SpanBuilder spanBuilder = tracer.buildSpan("create");

        if (activeSpan != null) {
            spanBuilder.asChildOf(activeSpan.context());
        }

        final Span span = spanBuilder.withTag("created", true).start();
        tracer.scopeManager().activate(span, true);

        entityManager.persist(book);
        span.finish();

        return book;
    }

    @Transactional(REQUIRED)
    public Book update(final Book book) {
        return entityManager.merge(book);
    }

    @Transactional(REQUIRED)
    @Traced
    public void deleteById(final Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
