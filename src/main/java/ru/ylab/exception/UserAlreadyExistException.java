package ru.ylab.exception;

public class UserAlreadyExistException extends BaseMonitoringServiceException {

    public UserAlreadyExistException(String userName) {
        super(String.format("User with name '%s' already exist", userName));
    }
}
