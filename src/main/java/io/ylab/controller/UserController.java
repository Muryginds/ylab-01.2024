package io.ylab.controller;

import lombok.RequiredArgsConstructor;
import io.ylab.dto.response.UserDTO;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.mapper.UserMapper;
import io.ylab.service.UserService;

/**
 * Controller class for handling user-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic.
 */
@RequiredArgsConstructor
public class UserController {
    /**
     * The associated service for user-related operations.
     */
    private final UserService userService;

    /**
     * Retrieves the details of the currently logged-in user.
     *
     * @return The UserDTO representing the current user.
     * @throws UserNotAuthorizedException If user is not authorized.
     */
    public UserDTO getCurrentUser() {
        var user = userService.getCurrentUser();
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Checks if a user with the specified ID exists.
     *
     * @param userId The ID of the user to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    public boolean checkUserExistsById(Long userId) {
        return userService.checkUserExistsById(userId);
    }

    /**
     * Checks if a user with the specified name exists.
     *
     * @param name The name of the user to check.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    public boolean checkUserExistsByName(String name) {
        return userService.checkUserExistsByName(name);
    }
}
