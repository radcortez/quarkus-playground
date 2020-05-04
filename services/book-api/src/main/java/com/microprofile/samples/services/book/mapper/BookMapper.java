package com.microprofile.samples.services.book.mapper;

import com.microprofile.samples.services.book.entity.Book;
import com.microprofile.samples.services.book.model.BookCreate;
import com.microprofile.samples.services.book.model.BookRead;
import com.microprofile.samples.services.book.model.BookUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookRead toBookRead(Book book);

    BookCreate toBookCreate(BookRead bookRead);

    BookUpdate toBookUpdate(BookRead bookRead);

    Book toBook(BookCreate bookCreate);

    Book toBook(BookUpdate bookUpdate);

    @Mapping(target = "id", ignore = true)
    Book toBook(Book source, @MappingTarget Book target);
}
