package io.ylab.backend.service;

import io.ylab.backend.dto.request.UserRegistrationRequestDto;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.commons.entity.User;
import io.ylab.commons.enumerated.UserRole;
import io.ylab.backend.exception.UserAlreadyExistException;
import io.ylab.backend.exception.UserNotAuthorizedException;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.security.JwtService;
import io.ylab.backend.utils.CurrentUserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        var requestDto = new UserRegistrationRequestDto("username", "password");
        var user = User.builder().name("username").password("encodedPassword").build();
        var expectedUserDto = UserDto.builder().build();
        when(userService.checkUserExistsByName("username")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toUserDto(user)).thenReturn(expectedUserDto);

        var actualUserDto = accountService.registerUser(requestDto);

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
        verify(userService, Mockito.times(1)).save(user);
        verify(meterService, Mockito.times(1)).generateForNewUser(user);
        verify(jwtService, Mockito.times(1)).generateToken(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists_ThrowsUserAlreadyExistException() {
        var requestDto = new UserRegistrationRequestDto("existingUsername", "password");
        when(userService.checkUserExistsByName("existingUsername")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> accountService.registerUser(requestDto));
        verify(userService, Mockito.never()).save(ArgumentMatchers.any());
        verify(meterService, Mockito.never()).generateForNewUser(ArgumentMatchers.any());
        verify(jwtService, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    void testGetCurrentUserDto_Successful() {
        var currentUser = User.builder().id(1L).name("user").role(UserRole.USER).build();
        var expectedUserDto = UserDto.builder().id(1L).name("user").build();
        when(userMapper.toUserDto(currentUser)).thenReturn(expectedUserDto);
        when(CurrentUserUtils.getCurrentUser()).thenReturn(currentUser);

        var actualUserDto = accountService.getCurrentUserDto();

        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void testGetCurrentUserDto_UserNotAuthorized_ThrowsUserNotAuthorizedException() {
        when(CurrentUserUtils.getCurrentUser()).thenThrow(new UserNotAuthorizedException());

        assertThrows(UserNotAuthorizedException.class, () -> accountService.getCurrentUserDto());
    }
}
