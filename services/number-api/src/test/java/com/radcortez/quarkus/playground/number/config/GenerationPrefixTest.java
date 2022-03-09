package com.radcortez.quarkus.playground.number.config;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerationPrefixTest {
    @Test
    void prefix() {
        Assertions.assertEquals(GenerationPrefix.BK, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
    }

    @Test
    void override() {
        System.setProperty("generation.prefix", "MV");
        Assertions.assertEquals(GenerationPrefix.MV, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
        System.clearProperty("generation.prefix");
    }

    @Test
    void undefined() {
        System.setProperty("generation.prefix", "X");
        Assertions.assertEquals(GenerationPrefix.UN, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
        System.clearProperty("generation.prefix");
    }
}
