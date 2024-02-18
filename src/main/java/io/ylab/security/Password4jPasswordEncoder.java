package io.ylab.security;

import com.password4j.Password;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of the PasswordEncoder interface that uses the Password4j library.
 * It provides methods for encoding and verifying passwords using the Argon2 hashing algorithm.
 */
@Component
public class Password4jPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return Password
                .hash(rawPassword)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Password.check(rawPassword, encodedPassword).withArgon2();
    }
}
