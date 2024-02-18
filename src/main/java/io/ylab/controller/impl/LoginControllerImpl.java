package io.ylab.controller.impl;

import io.ylab.controller.LoginController;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.dto.response.UserDto;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of controller responsible for handling user authentication and registration.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginController {
    private final LoginService loginService;

    /**
     * Authorizes a user based on the provided authorization request.
     *
     * @param requestDto The authorization request containing user credentials.
     * @return The UserDTO representing the authorized user.
     * @throws UserAuthenticationException If authentication fails.
     */
    @Override
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto authorize(@Valid @RequestBody UserAuthorizationRequestDto requestDto) {
        return loginService.authorize(requestDto);
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @throws UserNotAuthorizedException if user not authorized.
     */
    @Override
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDto logout() {
        return loginService.logout();
    }
}
