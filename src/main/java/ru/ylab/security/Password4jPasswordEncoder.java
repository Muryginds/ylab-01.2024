package ru.ylab.security;

import com.password4j.Password;

/**
 * Implementation of the PasswordEncoder interface that uses the Password4j library.
 * It provides methods for encoding and verifying passwords using the Argon2 hashing algorithm.
 */
public class Password4jPasswordEncoder implements PasswordEncoder {

    /**
     * Encodes the given plaintext password using the Argon2 hashing algorithm
     * with random salt and returns the resulting hash.
     *
     * @param password The plaintext password to be encoded.
     * @return The encoded password hash.
     */
    @Override
    public String encode(String password) {
        return Password
                .hash(password)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    /**
     * Verifies if the given plaintext password matches the provided hash.
     *
     * @param password The plaintext password to be verified.
     * @param hash     The hashed password to compare against.
     * @return True if the password matches the hash, false otherwise.
     */
    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).withArgon2();
    }
}
