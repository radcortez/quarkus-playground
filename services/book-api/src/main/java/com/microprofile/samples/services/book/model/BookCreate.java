package com.microprofile.samples.services.book.model;

import com.microprofile.samples.services.book.entity.Book;
import com.microprofile.samples.services.book.mapper.BookMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@NoArgsConstructor
@Getter(onMethod_ = {@Schema(hidden = true)})
@Data
public class BookCreate {
    private String author;
    private String title;
    private Integer year;
    private String genre;

    public Book toBook() {
        return BookMapper.INSTANCE.toBook(this);
    }
}
