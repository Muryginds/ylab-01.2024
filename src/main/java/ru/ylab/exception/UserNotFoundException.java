package ru.ylab.exception;

public class UserNotFoundException extends BaseMonitoringServiceException {

    public UserNotFoundException(Long userId) {
        super(String.format("User with id '%s' not found", userId));
    }
}
