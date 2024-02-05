package ru.ylab.exception;

public class MigrationException extends BaseMonitoringServiceException {
    public MigrationException(Exception e) {
        super(String.format("Migrations failed: %s", e.getMessage()));
    }
}
