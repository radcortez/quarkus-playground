package com.radcortez.quarkus.playground.services.book.entity;

import com.radcortez.quarkus.playground.services.book.mapper.BookMapper;
import com.radcortez.quarkus.playground.services.book.model.BookRead;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id")
    @SequenceGenerator(name = "id", sequenceName = "id")
    private Long id;
    private String author;
    private String title;
    private Integer publishYear;
    private String genre;
    private String isbn;

    @Builder
    public Book(final String author, final String title, final Integer publishYear, final String genre, final String isbn) {
        this.author = author;
        this.title = title;
        this.publishYear = publishYear;
        this.genre = genre;
        this.isbn = isbn;
    }

    public Book toBook(final Book book) {
        return BookMapper.INSTANCE.toBook(book, this);
    }

    public BookRead toBookRead() {
        return BookMapper.INSTANCE.toBookRead(this);
    }
}
