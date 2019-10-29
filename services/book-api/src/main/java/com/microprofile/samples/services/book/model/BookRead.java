package com.microprofile.samples.services.book.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@NoArgsConstructor
@Getter(onMethod_ = {@Schema(hidden = true)})
@Data
public class BookRead {
    private Long id;
    private String author;
    private String title;
    private Integer year;
    private String genre;
    private String isbn;
}
