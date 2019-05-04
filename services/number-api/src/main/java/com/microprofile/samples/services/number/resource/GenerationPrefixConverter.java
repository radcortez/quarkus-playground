package com.microprofile.samples.services.number.resource;

import org.eclipse.microprofile.config.spi.Converter;

import java.util.stream.Stream;

public class GenerationPrefixConverter implements Converter<GenerationPrefix> {
    @Override
    public GenerationPrefix convert(final String value) {
        return Stream.of(GenerationPrefix.values())
                     .filter(country -> country.name().compareToIgnoreCase(value) == 0)
                     .findFirst()
                     .orElse(GenerationPrefix.UN);
    }
}
