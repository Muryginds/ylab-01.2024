package io.ylab.exception;

public class UserNotFoundException extends BaseMonitoringServiceException {

    public UserNotFoundException(Long userId) {
        super(String.format("User with id '%s' not found", userId));
    }

    public UserNotFoundException(String userName) {
        super(String.format("User with name '%s' not found", userName));
    }
}
