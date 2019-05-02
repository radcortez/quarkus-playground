package com.microprofile.samples.services.book.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Schema(description = "The Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id")
    @SequenceGenerator(name = "id", sequenceName = "id")
    @Schema(description = "The id of the Book")
    private Long id;
    @Schema(description = "The author of the Book")
    private String author;
    @Schema(description = "The title of the Book")
    private String title;
    @Schema(description = "The year of the Book")
    private Integer year;
    @Schema(description = "The genre of the Book")
    private String genre;
    @Schema(description = "The isbn of the Book")
    private String isbn;

    public Book() {
    }

    public Book(final String author, final String title, final Integer year, final String genre, final String isbn) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Book book = (Book) o;

        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override public String toString() {
        return "Book{" +
               "id=" + id +
               ", author='" + author + '\'' +
               ", title='" + title + '\'' +
               ", year=" + year +
               ", genre='" + genre + '\'' +
               ", isbn='" + isbn + '\'' +
               '}';
    }
}
