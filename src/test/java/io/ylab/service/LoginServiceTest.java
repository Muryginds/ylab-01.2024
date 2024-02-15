package io.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.dto.request.UserRegistrationRequestDto;
import io.ylab.entity.User;
import io.ylab.exception.UserAlreadyExistException;
import io.ylab.exception.UserAuthenticationException;
import io.ylab.security.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditionEventService auditionEventService;

    @Mock
    private MeterService meterService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_whenNonExistingUser_thenReturnUserDTO() {
        var requestDTO = new UserRegistrationRequestDto("testUser", "password");
        when(userService.checkUserExistsByName(requestDTO.name())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPassword");

        var result = loginService.registerUser(requestDTO);

        assertNotNull(result);
        assertEquals(requestDTO.name(), result.name());
        verify(userService, Mockito.times(1)).save(Mockito.any(User.class));
        verify(meterService, Mockito.times(1)).generateForNewUser(Mockito.any(User.class));
    }

    @Test
    void testRegisterUser_whenExistingUserName_throwUserAlreadyExistException() {
        var requestDTO = new UserRegistrationRequestDto("existingUser", "password");
        when(userService.checkUserExistsByName(requestDTO.name())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> loginService.registerUser(requestDTO));
        verify(userService, Mockito.times(0)).save(Mockito.any());
        verify(meterService, Mockito.times(0)).generateForNewUser(Mockito.any());
        verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testAuthorize_whenExistingUser_thenReturnUserDTO() {
        var requestDTO = new UserAuthorizationRequestDto("testUser", "password");
        var user = User.builder().name("testUser").password("encodedPassword").build();
        when(userService.getUserByName(requestDTO.name())).thenReturn(user);
        when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(true);

        var result = loginService.authorize(requestDTO);

        assertNotNull(result);
        assertEquals(requestDTO.name(), result.name());
    }

    @Test
    void testAuthorize_whenNonExistingUser_throwUserAuthenticationException() {
        var requestDTO = new UserAuthorizationRequestDto("nonExistingUser", "password");
        when(userService.getUserByName(requestDTO.name())).thenThrow(new UserAuthenticationException());

        Assertions.assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDTO));
        verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testAuthorize_whenWrongPassword_throwUserAuthenticationException() {
        var requestDTO = new UserAuthorizationRequestDto("testUser", "wrongPassword");
        var user = User.builder().name("testUser").password("encodedPassword").build();
        when(userService.getUserByName(requestDTO.name())).thenThrow(new UserAuthenticationException());
        when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(false);

        Assertions.assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDTO));
        verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testLogout() {
        loginService.logout();
        assertNull(userService.getCurrentUser());
    }
}