package io.ylab.security;

public interface PasswordEncoder {
    String encode(String password);

    boolean verify(String password, String hash);
}
