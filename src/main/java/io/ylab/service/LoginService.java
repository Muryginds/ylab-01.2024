package io.ylab.service;

import lombok.RequiredArgsConstructor;
import io.ylab.annotation.Auditable;
import io.ylab.dto.response.UserDTO;
import io.ylab.dto.request.UserAuthorizationRequestDTO;
import io.ylab.dto.request.UserRegistrationRequestDTO;
import io.ylab.entity.User;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.UserAlreadyExistException;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.exception.UserNotFoundException;
import io.ylab.mapper.UserMapper;
import io.ylab.security.PasswordEncoder;

/**
 * Service responsible for user authentication, registration, and logout.
 */
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MeterService meterService;

    /**
     * Registers a new user with the specified registration details.
     * Generates user-specific meters.
     *
     * @param requestDTO The registration details of the new user.
     * @return UserDTO representing the registered user.
     * @throws UserAlreadyExistException If a user with the same name already exists.
     */
    @Auditable(eventType = AuditionEventType.REGISTRATION)
    public UserDTO registerUser(UserRegistrationRequestDTO requestDTO) {
        if (userService.checkUserExistsByName(requestDTO.name())) {
            throw new UserAlreadyExistException(requestDTO.name());
        }
        var user = User.builder()
                .name(requestDTO.name())
                .password(passwordEncoder.encode(requestDTO.password()))
                .build();
        userService.save(user);
        meterService.generateForNewUser(user);
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param requestDTO The user's authorization credentials.
     * @return UserDTO representing the authenticated user.
     * @throws UserAuthenticationException If authentication fails.
     */
    @Auditable(eventType = AuditionEventType.SESSION_START)
    public UserDTO authorize(UserAuthorizationRequestDTO requestDTO) {
        User user;
        try {
            user = userService.getUserByName(requestDTO.name());
        } catch (UserNotFoundException ex) {
            throw new UserAuthenticationException();
        }
        if (!passwordEncoder.verify(requestDTO.password(), user.getPassword())) {
            throw new UserAuthenticationException();
        }
        userService.setCurrentUser(user);
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Logs out the currently authenticated user.
     * @throws UserNotAuthorizedException if user not authorized.
     */
    @Auditable(eventType = AuditionEventType.SESSION_END)
    public void logout() {
        userService.setCurrentUser(null);
    }
}
