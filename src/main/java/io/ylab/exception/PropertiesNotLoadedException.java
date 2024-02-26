package io.ylab.exception;

public class PropertiesNotLoadedException extends BaseMonitoringServiceException {
    public PropertiesNotLoadedException(Exception e) {
        super(String.format("Properties not loaded: %s", e.getMessage()));
    }
}
