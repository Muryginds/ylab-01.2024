package ru.ylab.repository;

import ru.ylab.entity.User;

import java.util.Optional;

/**
 * Repository interface for managing user-related data operations.
 */
public interface UserRepository {

    /**
     * Checks if a user with the given username exists in the repository.
     *
     * @param username The username to check.
     * @return True if a user with the given username exists, false otherwise.
     */
    boolean checkUserExistsByName(String username);

    /**
     * Checks if a user with the given user ID exists in the repository.
     *
     * @param userId The user ID to check.
     * @return True if a user with the given user ID exists, false otherwise.
     */
    boolean checkUserExistsById(Long userId);

    /**
     * Saves a user in the repository.
     *
     * @param user The user to be saved.
     */
    void save(User user);

    /**
     * Finds a user by their username in the repository.
     *
     * @param name The username of the user to find.
     * @return An optional containing the found user or empty if not found.
     */
    Optional<User> findUserByName(String name);

    /**
     * Finds a user by their user ID in the repository.
     *
     * @param userId The user ID of the user to find.
     * @return An optional containing the found user or empty if not found.
     */
    Optional<User> findUserById(Long userId);
}
