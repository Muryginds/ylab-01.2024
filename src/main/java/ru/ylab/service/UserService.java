package ru.ylab.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ylab.dto.UserDTO;
import ru.ylab.entity.User;
import ru.ylab.exception.UserNotFoundException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.repository.UserRepository;

/**
 * The UserService class provides functionality related to user management.
 * It includes methods for user-related operations.
 * This service interacts with the UserRepository to perform various user-related tasks.
 */
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Getter
    @Setter
    private User currentUser;
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
     * Retrieves the UserDTO representing the currently authenticated user.
     *
     * @return UserDTO representing the currently authenticated user.
     */
    public UserDTO getCurrentUserDTO() {
        return UserMapper.MAPPER.toUserDTO(currentUser);
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
