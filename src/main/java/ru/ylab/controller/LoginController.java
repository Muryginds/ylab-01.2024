package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.UserDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.service.LoginService;

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
     */
    public UserDTO authorize(UserAuthorizationRequestDTO request) {
        return loginService.authorize(request);
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        loginService.logout();
    }
}
