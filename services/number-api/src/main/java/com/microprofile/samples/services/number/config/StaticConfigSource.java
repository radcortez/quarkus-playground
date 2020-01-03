package com.microprofile.samples.services.number.config;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;

public class StaticConfigSource implements ConfigSource {
    private static final Map<String, String> configuration = new HashMap<>();

    static {
        configuration.put("GENERATION_PREFIX", "BK");
    }

    @Override
    public int getOrdinal() {
        return 200;
    }

    @Override
    public Map<String, String> getProperties() {
        return configuration;
    }

    @Override
    public String getValue(final String propertyName) {
        return configuration.get(propertyName);
    }

    @Override
    public String getName() {
        return StaticConfigSource.class.getSimpleName();
    }
}
