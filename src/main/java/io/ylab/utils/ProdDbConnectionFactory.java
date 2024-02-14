package io.ylab.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static io.ylab.utils.PropertiesUtils.PropertiesType.DATABASE;

public class ProdDbConnectionFactory implements DbConnectionFactory {
    private static final Properties databaseProperties = PropertiesUtils.getProperties(DATABASE);

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                databaseProperties.getProperty("url"),
                databaseProperties.getProperty("username"),
                databaseProperties.getProperty("password"));
    }
}
