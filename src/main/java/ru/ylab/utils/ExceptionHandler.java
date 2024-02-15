package ru.ylab.utils;

import lombok.experimental.UtilityClass;
import ru.ylab.exception.MonitoringServiceSQLExceptionException;

import java.sql.SQLException;

@UtilityClass
public class ExceptionHandler {

    public void handleSQLException(SQLException e) {
        throw new MonitoringServiceSQLExceptionException(e);
    }
}
