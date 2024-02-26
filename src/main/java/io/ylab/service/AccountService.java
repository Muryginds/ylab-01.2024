package io.ylab.service;

import io.ylab.annotation.Auditable;
import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.dto.response.UserDto;
import io.ylab.entity.User;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.UserAlreadyExistException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.mapper.UserMapper;
import io.ylab.security.JwtService;
import io.ylab.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for account-related operations.
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MeterService meterService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    /**
     * Registers a new user account with the specified registration details.
     * Generates user-specific meters.
     *
     * @param requestDto The registration details of the new user.
     * @return UserDTO representing the registered user.
     * @throws UserAlreadyExistException If a user with the same name already exists.
     */
    @Transactional
    @Auditable(eventType = AuditionEventType.REGISTRATION)
    public UserDto registerUser(UserRegistrationRequestDto requestDto) {
        if (userService.checkUserExistsByName(requestDto.name())) {
            throw new UserAlreadyExistException(requestDto.name());
        }
        var user = User.builder()
                .name(requestDto.name())
                .password(passwordEncoder.encode(requestDto.password()))
                .build();
        userService.save(user);
        meterService.generateForNewUser(user);
        var jwtToken = jwtService.generateToken(user);
        var userDto = userMapper.toUserDto(user);
        userDto.setToken(jwtToken);
        return userDto;
    }

    /**
     * Retrieves the current authorized UserDto representing the user.
     *
     * @return UserDto representing the user with the specified username.
     * @throws UserNotAuthorizedException If user is not authorized.
     */
    public UserDto getCurrentUserDto() {
        return userMapper.toUserDto(CurrentUserUtils.getCurrentUser());
    }
}
