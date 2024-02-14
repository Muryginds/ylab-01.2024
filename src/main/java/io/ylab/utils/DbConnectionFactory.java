package io.ylab.utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnectionFactory {

    Connection getConnection() throws SQLException;
}
