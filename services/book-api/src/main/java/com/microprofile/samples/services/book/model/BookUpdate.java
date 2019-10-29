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

@Schema
public class BookUpdate {
    @Schema
    private String author;
    @Schema
    private String title;
    @Schema
    private Integer year;
    @Schema
    private String genre;

    public Book toBook() {
        return BookMapper.INSTANCE.toBook(this);
    }
}
