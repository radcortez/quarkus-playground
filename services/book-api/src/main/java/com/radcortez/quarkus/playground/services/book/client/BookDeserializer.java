package com.radcortez.quarkus.playground.services.book.client;

import com.radcortez.quarkus.playground.services.book.entity.Book;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class BookDeserializer extends JsonbDeserializer<Book> {
    public BookDeserializer() {
        super(Book.class);
    }
}
