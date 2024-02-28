package io.ylab.backend.exception;

public class MeterTypeNotFoundException extends BaseMonitoringServiceException {

    public MeterTypeNotFoundException(Long meterTypeId) {
        super(String.format("MeterType with id '%s' not found", meterTypeId));
    }
}
