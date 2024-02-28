package io.ylab.backend.exception;

public class BaseMonitoringServiceException extends RuntimeException {

    public BaseMonitoringServiceException (String exceptionText) {
        super(exceptionText);
    }
}
