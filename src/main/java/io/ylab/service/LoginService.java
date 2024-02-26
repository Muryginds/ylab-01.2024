package io.ylab.service;

import io.ylab.annotation.Auditable;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.dto.response.UserDto;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.mapper.UserMapper;
import io.ylab.security.JwtService;
import io.ylab.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication and logout.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param requestDto The user's authorization credentials.
     * @return UserDTO representing the authenticated user.
     * @throws UserAuthenticationException If authentication fails.
     */
    @Auditable(eventType = AuditionEventType.SESSION_START)
    public UserDto authorize(UserAuthorizationRequestDto requestDto) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(requestDto.name(), requestDto.password());
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException();
        }
        var user = userService.getUserByName(requestDto.name());
        var token = jwtService.generateToken(user);
        var userDto = userMapper.toUserDto(user);
        userDto.setToken(token);
        return userDto;
    }

    /**
     * Logs out the currently authenticated user.
     * @throws UserNotAuthorizedException if user not authorized.
     */
    @Auditable(eventType = AuditionEventType.SESSION_END)
    public MessageDto logout() {
        return ResponseUtils.responseWithMessage("User logged out");
    }
}
