package io.ylab.controller;

import io.ylab.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.service.LoginService;

/**
 * Controller responsible for handling user authentication and registration.
 */
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param requestDto The registration request containing user details.
     * @return The UserDTO representing the registered user.
     */
    public UserDto register(UserRegistrationRequestDto requestDto) {
        return loginService.registerUser(requestDto);
    }

    /**
     * Authorizes a user based on the provided authorization request.
     *
     * @param requestDto The authorization request containing user credentials.
     * @return The UserDTO representing the authorized user.
     * @throws UserAuthenticationException If authentication fails.
     */
    public UserDto authorize(UserAuthorizationRequestDto requestDto) {
        return loginService.authorize(requestDto);
    }

    /**
     * Logs out the currently logged-in user.
     * @throws UserNotAuthorizedException if user not authorized.
     */
    public void logout() {
        loginService.logout();
    }
}
