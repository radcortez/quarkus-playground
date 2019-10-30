package com.microprofile.samples.services.book.entity;

import com.microprofile.samples.services.book.mapper.BookMapper;
import com.microprofile.samples.services.book.model.BookRead;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
    private Integer year;
    private String genre;
    private String isbn;

    @Builder
    public Book(final String author, final String title, final Integer year, final String genre, final String isbn) {
        this.author = author;
        this.title = title;
        this.year = year;
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
