package com.radcortez.quarkus.playground.number.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "number")
public interface NumberConfig {
    Generation generation();

    interface Generation {
        @WithDefault("BK")
        Prefix prefix();
    }
}
