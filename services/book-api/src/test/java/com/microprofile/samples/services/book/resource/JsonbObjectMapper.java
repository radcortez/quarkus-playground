package com.microprofile.samples.services.book.resource;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonbObjectMapper implements ObjectMapper {
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    public Object deserialize(final ObjectMapperDeserializationContext context) {
        return jsonb.fromJson(context.getDataToDeserialize().asInputStream(), context.getType());
    }

    @Override
    public Object serialize(final ObjectMapperSerializationContext context) {
        return jsonb.toJson(context.getObjectToSerialize());
    }
}
