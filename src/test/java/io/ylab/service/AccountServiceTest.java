package io.ylab.service;

import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.dto.response.UserDto;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.UserAlreadyExistException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.mapper.UserMapper;
import io.ylab.security.JwtService;
import io.ylab.utils.CurrentUserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MeterService meterService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    private MockedStatic<CurrentUserUtils> utilsMockedStatic;

    @BeforeEach
    void setUp() {
        utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class);
    }

    @AfterEach
    void tearDown() {
        utilsMockedStatic.close();
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("username", "password");
        User user = User.builder().name("username").password("encodedPassword").build();
        UserDto expectedUserDto = UserDto.builder().build();
        when(userService.checkUserExistsByName("username")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toUserDto(user)).thenReturn(expectedUserDto);

        UserDto actualUserDto = accountService.registerUser(requestDto);

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
        verify(userService, times(1)).save(user);
        verify(meterService, times(1)).generateForNewUser(user);
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists_ThrowsUserAlreadyExistException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("existingUsername", "password");
        when(userService.checkUserExistsByName("existingUsername")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> accountService.registerUser(requestDto));
        verify(userService, never()).save(any());
        verify(meterService, never()).generateForNewUser(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void testGetCurrentUserDto_Successful() {
        User currentUser = User.builder().id(1L).name("user").role(UserRole.USER).build();
        UserDto expectedUserDto = UserDto.builder().id(1L).name("user").build();
        when(userMapper.toUserDto(currentUser)).thenReturn(expectedUserDto);
        when(CurrentUserUtils.getCurrentUser()).thenReturn(currentUser);

        UserDto actualUserDto = accountService.getCurrentUserDto();

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void testGetCurrentUserDto_UserNotAuthorized_ThrowsUserNotAuthorizedException() {
        when(CurrentUserUtils.getCurrentUser()).thenThrow(new UserNotAuthorizedException());

        assertThrows(UserNotAuthorizedException.class, () -> accountService.getCurrentUserDto());
    }
}
