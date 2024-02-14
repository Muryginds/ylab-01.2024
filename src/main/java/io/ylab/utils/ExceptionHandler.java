package io.ylab.utils;

import lombok.experimental.UtilityClass;
import io.ylab.exception.MonitoringServiceSQLExceptionException;

import java.sql.SQLException;

@UtilityClass
public class ExceptionHandler {

    public void handleSQLException(SQLException e) {
        throw new MonitoringServiceSQLExceptionException(e);
    }
}
