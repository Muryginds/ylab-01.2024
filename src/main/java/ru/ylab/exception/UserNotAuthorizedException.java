package ru.ylab.exception;

public class UserNotAuthorizedException extends BaseMonitoringServiceException {

    public UserNotAuthorizedException() {
        super("User not authorized");
    }
}

