package com.microprofile.samples.services.number.resource;

import org.apache.commons.dbutils.DbUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigSource implements ConfigSource {
    private DataSource dataSource;

    public DatabaseConfigSource() {
        try {
            dataSource = (DataSource) new InitialContext().lookup("openejb:Resource/config-source-database");
        } catch (final NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int getOrdinal() {
        return 500;
    }

    @Override
    public Map<String, String> getProperties() {
        final Map<String, String> properties = new HashMap<>();

        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement query = connection.prepareStatement("SELECT NAME, VALUE FROM CONFIGURATIONS");
            final ResultSet names = query.executeQuery();

            while (names.next()) {
                properties.put(names.getString(0), names.getString(1));
            }

            DbUtils.closeQuietly(names);
            DbUtils.closeQuietly(query);
            DbUtils.closeQuietly(connection);
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return properties;
    }

    @Override
    public String getValue(final String propertyName) {
        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement query =
                    connection.prepareStatement("SELECT VALUE FROM CONFIGURATIONS WHERE NAME = ?");
            query.setString(1, propertyName);
            final ResultSet value = query.executeQuery();

            if (value.next()) {
                return value.getString(1);
            }

            DbUtils.closeQuietly(value);
            DbUtils.closeQuietly(query);
            DbUtils.closeQuietly(connection);
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return DatabaseConfigSource.class.getSimpleName();
    }
}
