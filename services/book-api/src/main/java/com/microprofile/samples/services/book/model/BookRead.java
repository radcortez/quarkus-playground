package com.microprofile.samples.services.book.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@NoArgsConstructor
@Getter(onMethod_ = {@Schema(hidden = true)})
@Data

@Schema
public class BookRead {
    @Schema
    private Long id;
    @Schema
    private String author;
    @Schema
    private String title;
    @Schema
    private Integer year;
    @Schema
    private String genre;
    @Schema
    private String isbn;
}
