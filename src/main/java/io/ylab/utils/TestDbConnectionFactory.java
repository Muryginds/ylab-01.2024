package io.ylab.utils;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TestDbConnectionFactory implements DbConnectionFactory {
    private final String url;
    private final String name;
    private final String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, name, password);
    }
}
