package io.ylab.backend.service;

import io.ylab.backend.dto.request.UserAuthorizationRequestDto;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.commons.entity.User;
import io.ylab.backend.exception.UserAuthenticationException;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.security.JwtService;
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
        var user = User.builder().build();
        var token = "jwtToken";
        var expectedUserDto = UserDto.builder().build();
        when(userService.getUserByName("username")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(userMapper.toUserDto(user)).thenReturn(expectedUserDto);

        var actualUserDto = loginService.authorize(requestDto);

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
        assertEquals(token, actualUserDto.getToken());
    }

    @Test
    void testAuthorize_AuthenticationFailure_ThrowsUserAuthenticationException() {
        var requestDto = new UserAuthorizationRequestDto("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(UserAuthenticationException.class);

        assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDto));
    }

    @Test
    void testLogout_Successful() {
        var messageDto = loginService.logout();

        assertNotNull(messageDto);
        assertEquals("User logged out", messageDto.message());
    }
}
