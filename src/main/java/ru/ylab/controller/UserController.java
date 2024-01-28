package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.UserDTO;
import ru.ylab.in.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.in.dto.request.UserRegistrationRequestDTO;
import ru.ylab.service.UserService;

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
    public final UserService userService;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return The UserDTO representing the registered user.
     */
    public UserDTO register(UserRegistrationRequestDTO request) {
        return userService.registerUser(request);
    }

    /**
     * Authorizes a user based on the provided authorization request.
     *
     * @param request The authorization request containing user credentials.
     * @return The UserDTO representing the authorized user.
     */
    public UserDTO authorize(UserAuthorizationRequestDTO request) {
        return userService.authorize(request);
    }

    /**
     * Retrieves the details of the currently logged-in user.
     *
     * @return The UserDTO representing the current user.
     */
    public UserDTO getCurrentUser() {
        return userService.getCurrentUserDTO();
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

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        userService.logout();
    }
}
