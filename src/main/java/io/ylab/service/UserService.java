package io.ylab.service;

import lombok.RequiredArgsConstructor;
import io.ylab.entity.User;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.exception.UserNotFoundException;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.UserRepository;

/**
 * The UserService class provides functionality related to user management.
 * It includes methods for user-related operations.
 * This service interacts with the UserRepository to perform various user-related tasks.
 */
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private User sessionedUser;

    /**
     * Retrieves the current authorized User entity representing the user.
     *
     * @return User entity representing the user with the specified username.
     * @throws UserNotAuthorizedException If user is not authorized.
     */
    public User getCurrentUser() {
        if (sessionedUser == null) {
            throw new UserNotAuthorizedException();
        }
        return sessionedUser;
    }

    public void setCurrentUser(User user) {
        sessionedUser = user;
    }

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username The username to check.
     * @return true if the user with the given username exists, false otherwise.
     */
    public boolean checkUserExistsByName(String username) {
        return userRepository.checkUserExistsByName(username);
    }

    /**
     * Checks if a user with the given user ID exists.
     *
     * @param userId The user ID to check.
     * @return true if the user with the given ID exists, false otherwise.
     */
    public boolean checkUserExistsById(Long userId) {
        return userRepository.checkUserExistsById(userId);
    }

    /**
     * Retrieves the User entity representing the user with the specified user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return User entity representing the user with the specified user ID.
     * @throws UserNotFoundException If user is not found.
     */
    public User getUserById(Long userId) {
        var userModel = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return UserMapper.MAPPER.toUser(userModel);
    }

    /**
     * Retrieves the User entity representing the user with the specified username.
     *
     * @param userName The name of the user to retrieve.
     * @return User entity representing the user with the specified username.
     * @throws UserNotFoundException If user is not found.
     */
    public User getUserByName(String userName) {
        var userModel = userRepository.findUserByName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
        return UserMapper.MAPPER.toUser(userModel);
    }

    /**
     * Saves a user.
     *
     * @param user The user to be saved.
     */
    public void save(User user) {
        userRepository.save(user);
    }
}
