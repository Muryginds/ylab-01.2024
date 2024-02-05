package ru.ylab.exception;

public class MonitoringServiceSQLExceptionException extends BaseMonitoringServiceException {
    public MonitoringServiceSQLExceptionException(Exception e) {
        super(String.format("SQL Exception: %s", e.getMessage()));
    }
}
