package ru.ylab.utils;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static ru.ylab.utils.PropertiesUtils.PropertiesType.DATABASE;

@UtilityClass
public class DbConnectionUtils {
    private static final Properties databaseProperties = PropertiesUtils.getProperties(DATABASE);

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                databaseProperties.getProperty("url"),
                databaseProperties.getProperty("username"),
                databaseProperties.getProperty("password"));
    }
}
