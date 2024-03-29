package io.ylab.backend.controller.impl;

import io.ylab.backend.controller.AccountController;
import io.ylab.backend.dto.request.UserRegistrationRequestDto;
import io.ylab.backend.exception.UserNotAuthorizedException;
import io.ylab.backend.service.AccountService;
import io.ylab.backend.dto.response.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Implementation of controller responsible for account-related operations and interactions.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {
    private final AccountService accountService;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param requestDto The registration request containing user details.
     * @return The UserDTO representing the registered user.
     */
    @Override
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto register(@Valid @RequestBody UserRegistrationRequestDto requestDto) {
        return accountService.registerUser(requestDto);
    }

    /**
     * Retrieves the details of the currently logged-in user.
     *
     * @return The UserDTO representing the current user.
     * @throws UserNotAuthorizedException If user is not authorized.
     */
    @Override
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getCurrentUser() {
        return accountService.getCurrentUserDto();
    }
}
