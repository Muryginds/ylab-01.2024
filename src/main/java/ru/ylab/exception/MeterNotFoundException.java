package ru.ylab.exception;

public class MeterNotFoundException extends BaseMonitoringServiceException {

    public MeterNotFoundException(Long meterId) {
        super(String.format("Meter with id '%s' not found", meterId));
    }
}
