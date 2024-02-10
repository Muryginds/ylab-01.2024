package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.service.LoginService;

/**
 * Controller responsible for handling user authentication and registration.
 */
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return The UserDTO representing the registered user.
     */
    public UserDTO register(UserRegistrationRequestDTO request) {
        return loginService.registerUser(request);
    }

    /**
     * Authorizes a user based on the provided authorization request.
     *
     * @param request The authorization request containing user credentials.
     * @return The UserDTO representing the authorized user.
     * @throws UserAuthenticationException If authentication fails.
     */
    public UserDTO authorize(UserAuthorizationRequestDTO request) {
        return loginService.authorize(request);
    }

    /**
     * Logs out the currently logged-in user.
     * @throws UserNotAuthorizedException if user not authorized.
     */
    public void logout() {
        loginService.logout();
    }
}
