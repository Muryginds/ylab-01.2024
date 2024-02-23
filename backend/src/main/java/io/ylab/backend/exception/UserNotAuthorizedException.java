package io.ylab.backend.exception;

public class UserNotAuthorizedException extends BaseMonitoringServiceException {

    public UserNotAuthorizedException() {
        super("User not authorized");
    }
}

