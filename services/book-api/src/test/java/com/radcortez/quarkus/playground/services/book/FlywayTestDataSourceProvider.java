package com.radcortez.quarkus.playground.services.book;

import com.radcortez.flyway.test.junit.DataSourceInfo;
import com.radcortez.flyway.test.junit.DataSourceProvider;
import io.smallrye.config.Config;
import org.junit.jupiter.api.extension.ExtensionContext;

public class FlywayTestDataSourceProvider implements DataSourceProvider {
    @Override
    public DataSourceInfo getDatasourceInfo(final ExtensionContext extensionContext) {
        Config config = Config.get();
        return DataSourceInfo.config(
                config.getValue("quarkus.datasource.jdbc.url", String.class),
                config.getValue("quarkus.datasource.username", String.class),
                config.getValue("quarkus.datasource.password", String.class));
    }
}
