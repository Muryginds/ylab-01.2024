package ru.ylab.exception;

public class NoSubmissionsException extends BaseMonitoringServiceException {

    public NoSubmissionsException(String username) {
        super(String.format("No submissions found for user '%s'", username));
    }
}
