package io.ylab.backend.service;

import io.ylab.backend.annotation.Auditable;
import io.ylab.backend.dto.request.UserAuthorizationRequestDto;
import io.ylab.backend.entity.User;
import io.ylab.backend.enumerated.AuditionEventType;
import io.ylab.backend.exception.UserAuthenticationException;
import io.ylab.backend.exception.UserNotAuthorizedException;
import io.ylab.backend.security.JwtService;
import io.ylab.backend.utils.ResponseUtils;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User user;
        try {
            user = userService.getUserByName(requestDto.name());
            var authentication = new UsernamePasswordAuthenticationToken(user, requestDto.password());
            authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException();
        }
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
