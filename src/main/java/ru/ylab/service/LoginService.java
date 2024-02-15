package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.annotation.Auditable;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.entity.User;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.exception.UserNotFoundException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.security.PasswordEncoder;

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
