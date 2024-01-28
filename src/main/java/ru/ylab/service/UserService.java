package ru.ylab.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.User;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.exception.UserNotFoundException;
import ru.ylab.in.dto.UserDTO;
import ru.ylab.in.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.in.dto.request.UserRegistrationRequestDTO;
import ru.ylab.mapper.UserMapper;
import ru.ylab.repository.UserRepository;
import ru.ylab.security.PasswordEncoder;

/**
 * The UserService class provides functionality related to user management.
 * It includes methods for user registration, user authentication, and other user-related operations.
 * This service interacts with the UserRepository, PasswordEncoder, AuditionEventService, and MeterService
 * to perform various user-related tasks.
 */
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditionEventService auditionEventService;
    private final MeterService meterService;

    @Getter
    private User currentUser;

    /**
     * Registers a new user with the specified registration details.
     * Generates user-specific meters and adds an audition event for the registration.
     *
     * @param requestDTO The registration details of the new user.
     * @return UserDTO representing the registered user.
     * @throws UserAlreadyExistException If a user with the same name already exists.
     */
    public UserDTO registerUser(UserRegistrationRequestDTO requestDTO) {
        if (!checkUserExistsByName(requestDTO.name())) {
            var user = User.builder()
                    .name(requestDTO.name())
                    .password(passwordEncoder.encode(requestDTO.password()))
                    .build();
            userRepository.save(user);
            meterService.generateForNewUser(user);
            var event = AuditionEvent.builder()
                    .user(user)
                    .type(AuditionEventType.REGISTRATION)
                    .message("user registered")
                    .build();
            auditionEventService.addEvent(event);
            return UserMapper.MAPPER.toUserDTO(user);
        }
        throw new UserAlreadyExistException(requestDTO.name());
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
        var user = userRepository.findUserByName(requestDTO.name())
                .orElseThrow(UserAuthenticationException::new);
        if (!passwordEncoder.verify(requestDTO.password(), user.getPassword())) {
            throw new UserAuthenticationException();
        }
        var event = AuditionEvent.builder()
                .user(user)
                .type(AuditionEventType.SESSION_START)
                .message("user authorized")
                .build();
        auditionEventService.addEvent(event);
        currentUser = user;
        return UserMapper.MAPPER.toUserDTO(user);
    }

    /**
     * Logs out the currently authenticated user.
     * Adds an audition event for the logout.
     */
    public void logout() {
        var event = AuditionEvent.builder()
                .user(currentUser)
                .type(AuditionEventType.SESSION_END)
                .message("user logged out")
                .build();
        auditionEventService.addEvent(event);
        currentUser = null;
    }

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username The username to check.
     * @return true if the user with the given username exists, false otherwise.
     */
    public boolean checkUserExistsByName(String username) {
        return userRepository.checkUserExistsByName(username);
    }

    /**
     * Checks if a user with the given user ID exists.
     *
     * @param userId The user ID to check.
     * @return true if the user with the given ID exists, false otherwise.
     */
    public boolean checkUserExistsById(Long userId) {
        return userRepository.checkUserExistsById(userId);
    }

    /**
     * Retrieves the UserDTO representing the currently authenticated user.
     *
     * @return UserDTO representing the currently authenticated user.
     */
    public UserDTO getCurrentUserDTO() {
        return UserMapper.MAPPER.toUserDTO(currentUser);
    }

    /**
     * Retrieves the User entity representing the user with the specified user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return User entity representing the user with the specified user ID.
     */
    public User getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
