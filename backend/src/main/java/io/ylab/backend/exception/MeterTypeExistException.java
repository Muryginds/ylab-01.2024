package io.ylab.backend.exception;

public class MeterTypeExistException extends BaseMonitoringServiceException {

    public MeterTypeExistException(String typeName) {
        super(String.format("Typename with name '%s' exists", typeName));
    }
}
