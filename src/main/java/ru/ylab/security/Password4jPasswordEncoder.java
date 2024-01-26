package ru.ylab.security;

import com.password4j.Password;

public class Password4jPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        return Password
                .hash(password)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).withArgon2();
    }
}
