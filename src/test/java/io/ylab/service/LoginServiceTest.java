package io.ylab.service;

import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.dto.response.UserDto;
import io.ylab.entity.User;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.mapper.UserMapper;
import io.ylab.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginService loginService;

    @Test
    void testAuthorize_SuccessfulAuthorization() {
        var requestDto = new UserAuthorizationRequestDto("username", "password");
        User user = User.builder().build();
        String token = "jwtToken";
        UserDto expectedUserDto = UserDto.builder().build();
        when(userService.getUserByName("username")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(userMapper.toUserDto(user)).thenReturn(expectedUserDto);

        UserDto actualUserDto = loginService.authorize(requestDto);

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
        assertEquals(token, actualUserDto.getToken());
    }

    @Test
    void testAuthorize_AuthenticationFailure_ThrowsUserAuthenticationException() {
        UserAuthorizationRequestDto requestDto = new UserAuthorizationRequestDto("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(UserAuthenticationException.class);

        assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDto));
    }

    @Test
    void testLogout_Successful() {
        MessageDto messageDto = loginService.logout();

        assertNotNull(messageDto);
        assertEquals("User logged out", messageDto.message());
    }
}
