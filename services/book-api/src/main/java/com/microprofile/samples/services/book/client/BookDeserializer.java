package com.microprofile.samples.services.book.client;

import com.microprofile.samples.services.book.entity.Book;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class BookDeserializer extends JsonbDeserializer<Book> {
    public BookDeserializer() {
        super(Book.class);
    }
}
