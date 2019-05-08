package com.microprofile.samples.clients.standalone;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Builder
public class Book {
    private final Long id;
    private final String author;
    private final String title;
    private final Integer year;
    private final String genre;
    private final String isbn;
}
