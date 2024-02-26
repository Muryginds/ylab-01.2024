package io.ylab.exception;

public class NoPermissionException extends BaseMonitoringServiceException {

    public NoPermissionException() {
        super("Current user has no permission to get this information");
    }
}
