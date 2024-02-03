package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.UserDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.User;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.exception.UserNotFoundException;
import ru.ylab.mapper.UserMapper;
import ru.ylab.security.PasswordEncoder;

@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuditionEventService auditionEventService;
    private final MeterService meterService;

    /**
     * Registers a new user with the specified registration details.
     * Generates user-specific meters and adds an audition event for the registration.
     *
     * @param requestDTO The registration details of the new user.
     * @return UserDTO representing the registered user.
     * @throws UserAlreadyExistException If a user with the same name already exists.
     */
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
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(AuditionEventType.REGISTRATION)
                .message("user registered")
                .build();
        auditionEventService.addEvent(event);
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Authenticates a user with the provided credentials.
     * Adds an audition event for a successful authentication.
     *
     * @param requestDTO The user's authorization credentials.
     * @return UserDTO representing the authenticated user.
     * @throws UserAuthenticationException If authentication fails.
     */
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
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(AuditionEventType.SESSION_START)
                .message("user authorized")
                .build();
        auditionEventService.addEvent(event);
        userService.setCurrentUser(user);
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Logs out the currently authenticated user.
     * Adds an audition event for the logout.
     */
    public void logout() {
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .eventType(AuditionEventType.SESSION_END)
                .message("user logged out")
                .build();
        auditionEventService.addEvent(event);
        userService.setCurrentUser(null);
    }
}
