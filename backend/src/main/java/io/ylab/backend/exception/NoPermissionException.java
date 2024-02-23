package io.ylab.backend.exception;

public class NoPermissionException extends BaseMonitoringServiceException {

    public NoPermissionException() {
        super("Current user has no permission to get this information");
    }
}
