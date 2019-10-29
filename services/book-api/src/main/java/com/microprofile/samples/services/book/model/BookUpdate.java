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

@Schema(description = "The book to update")
public class BookUpdate {
    @Schema(
        description = "The author of the book",
        example = "Roberto Cortez"
    )
    private String author;

    @Schema(
        description = "The title of the book",
        example = "MicroProfile in Action"
    )
    private String title;

    @Schema(
        description = "The publishing year of the book",
        example = "2020"
    )
    private Integer year;

    @Schema(
        description = "The genre of the book",
        example = "Tech"
    )
    private String genre;

    public Book toBook() {
        return BookMapper.INSTANCE.toBook(this);
    }
}
