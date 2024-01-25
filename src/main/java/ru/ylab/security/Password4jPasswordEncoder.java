package ru.ylab.security;

import com.password4j.Password;

public class Password4jPasswordEncoder implements PasswordEncoder {
    private static final String SECRET_KEY = "superSecret";

    @Override
    public String encode(String password) {
        return Password
                .hash(password)
                .addSalt(SECRET_KEY)
                .withArgon2()
                .getResult();
    }

    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).addSalt(SECRET_KEY).withArgon2();
    }
}
