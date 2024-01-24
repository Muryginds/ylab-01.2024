package ru.ylab.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String name) {
        super(String.format("User with name '%s' already exist", name));
    }
}
