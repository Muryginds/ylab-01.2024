package ru.ylab.exception;

public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException() {
        super("Username or password is invalid");
    }
}
