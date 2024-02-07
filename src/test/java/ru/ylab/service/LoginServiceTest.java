package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.entity.User;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.security.PasswordEncoder;

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
        var requestDTO = new UserRegistrationRequestDTO("testUser", "password");
        Mockito.when(userService.checkUserExistsByName(requestDTO.name())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPassword");

        var result = loginService.registerUser(requestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDTO.name(), result.name());
        Mockito.verify(userService, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(meterService, Mockito.times(1)).generateForNewUser(Mockito.any(User.class));
        Mockito.verify(auditionEventService, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testRegisterUser_whenExistingUserName_throwUserAlreadyExistException() {
        var requestDTO = new UserRegistrationRequestDTO("existingUser", "password");
        Mockito.when(userService.checkUserExistsByName(requestDTO.name())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class, () -> loginService.registerUser(requestDTO));
        Mockito.verify(userService, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(meterService, Mockito.times(0)).generateForNewUser(Mockito.any());
        Mockito.verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testAuthorize_whenExistingUser_thenReturnUserDTO() {
        var requestDTO = new UserAuthorizationRequestDTO("testUser", "password");
        var user = User.builder().name("testUser").password("encodedPassword").build();
        Mockito.when(userService.getUserByName(requestDTO.name())).thenReturn(user);
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(true);

        var result = loginService.authorize(requestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDTO.name(), result.name());
        Mockito.verify(auditionEventService, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testAuthorize_whenNonExistingUser_throwUserAuthenticationException() {
        var requestDTO = new UserAuthorizationRequestDTO("nonExistingUser", "password");
        Mockito.when(userService.getUserByName(requestDTO.name())).thenThrow(new UserAuthenticationException());

        Assertions.assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDTO));
        Mockito.verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testAuthorize_whenWrongPassword_throwUserAuthenticationException() {
        var requestDTO = new UserAuthorizationRequestDTO("testUser", "wrongPassword");
        var user = User.builder().name("testUser").password("encodedPassword").build();
        Mockito.when(userService.getUserByName(requestDTO.name())).thenThrow(new UserAuthenticationException());
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(false);

        Assertions.assertThrows(UserAuthenticationException.class, () -> loginService.authorize(requestDTO));
        Mockito.verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testLogout() {
        loginService.logout();

        Mockito.verify(auditionEventService, Mockito.times(1)).save(Mockito.any());
    }
}