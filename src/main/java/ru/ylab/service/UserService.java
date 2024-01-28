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

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditionEventService auditionEventService;
    private final MeterService meterService;

    @Getter
    private User currentUser;

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

    public void logout() {
        var event = AuditionEvent.builder()
                .user(currentUser)
                .type(AuditionEventType.SESSION_END)
                .message("user logged out")
                .build();
        auditionEventService.addEvent(event);
        currentUser = null;
    }

    public boolean checkUserExistsByName(String username) {
        return userRepository.checkUserExistsByName(username);
    }

    public boolean checkUserExistsById(Long userId) {
        return userRepository.checkUserExistsById(userId);
    }

    public UserDTO getCurrentUserDTO() {
        return UserMapper.MAPPER.toUserDTO(currentUser);
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
