package io.ylab.backend.service;

import io.ylab.commons.entity.User;
import io.ylab.backend.exception.UserNotFoundException;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The UserService class provides functionality related to user management.
 * It includes methods for user-related operations.
 * This service interacts with the UserRepository to perform various user-related tasks.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
        return userMapper.toUser(userModel);
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
        return userMapper.toUser(userModel);
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
