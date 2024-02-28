package io.ylab.backend.exception;

public class MeterNotFoundException extends BaseMonitoringServiceException {

    public MeterNotFoundException(Long meterId) {
        super(String.format("Meter with id '%s' not found", meterId));
    }
}
