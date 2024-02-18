package io.ylab.controller.impl;

import io.ylab.controller.AccountController;
import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.dto.response.UserDto;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.service.AccountService;
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
