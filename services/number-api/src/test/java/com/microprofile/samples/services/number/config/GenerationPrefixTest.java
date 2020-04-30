package com.microprofile.samples.services.number.config;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

import static com.microprofile.samples.services.number.config.GenerationPrefix.BK;
import static com.microprofile.samples.services.number.config.GenerationPrefix.MV;
import static com.microprofile.samples.services.number.config.GenerationPrefix.UN;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerationPrefixTest {
    @Test
    void prefix() {
        assertEquals(BK, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
    }

    @Test
    void override() {
        System.setProperty("generation.prefix", "MV");
        assertEquals(MV, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
        System.clearProperty("generation.prefix");
    }

    @Test
    void undefined() {
        System.setProperty("generation.prefix", "X");
        assertEquals(UN, ConfigProvider.getConfig().getValue("generation.prefix", GenerationPrefix.class));
        System.clearProperty("generation.prefix");
    }
}
