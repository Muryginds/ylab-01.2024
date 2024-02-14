package io.ylab.exception;

public class UserAuthenticationException extends BaseMonitoringServiceException {

    public UserAuthenticationException() {
        super("Username or password is invalid");
    }
}
