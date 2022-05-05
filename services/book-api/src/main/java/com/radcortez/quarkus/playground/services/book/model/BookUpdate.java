package com.radcortez.quarkus.playground.services.book.model;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import com.radcortez.quarkus.playground.services.book.mapper.BookMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@Getter(onMethod_ = {@Schema(hidden = true)})
@Data
@Builder(toBuilder = true)

@Schema(name = "bookUpdate", description = "The book to update")
public class BookUpdate {
    @Schema(
        description = "The author of the book",
        example = "Roberto Cortez"
    )
    private String author;

    @Schema(
        description = "The title of the book",
        example = "Quarkus in Action"
    )
    private String title;

    @Schema(
        description = "The publishing year of the book",
        example = "2020"
    )
    private Integer publishYear;

    @Schema(
        description = "The genre of the book",
        example = "Tech"
    )
    private String genre;

    public Book toBook() {
        return BookMapper.INSTANCE.toBook(this);
    }
}
