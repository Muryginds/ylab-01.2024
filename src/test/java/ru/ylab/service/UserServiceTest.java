package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.entity.User;
import ru.ylab.exception.UserAlreadyExistException;
import ru.ylab.exception.UserAuthenticationException;
import ru.ylab.dto.UserDTO;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.repository.UserRepository;
import ru.ylab.security.PasswordEncoder;

import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditionEventService auditionEventService;

    @Mock
    private MeterService meterService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_whenNonExistingUser_thenReturnUserDTO() {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("testUser", "password");
        Mockito.when(userRepository.checkUserExistsByName(requestDTO.name())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPassword");

        UserDTO result = userService.registerUser(requestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDTO.name(), result.name());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(meterService, Mockito.times(1)).generateForNewUser(Mockito.any(User.class));
        Mockito.verify(auditionEventService, Mockito.times(1)).addEvent(Mockito.any());
    }

    @Test
    void testRegisterUser_whenExistingUserName_throwUserAlreadyExistException() {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("existingUser", "password");
        Mockito.when(userRepository.checkUserExistsByName(requestDTO.name())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(requestDTO));
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(meterService, Mockito.times(0)).generateForNewUser(Mockito.any());
        Mockito.verify(auditionEventService, Mockito.times(0)).addEvent(Mockito.any());
    }

    @Test
    void testAuthorize_whenExistingUser_thenReturnUserDTO() {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("testUser", "password");
        User user = User.builder().name("testUser").password("encodedPassword").build();
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(true);

        UserDTO result = userService.authorize(requestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDTO.name(), result.name());
        Mockito.verify(auditionEventService, Mockito.times(1)).addEvent(Mockito.any());
    }

    @Test
    void testAuthorize_whenNonExistingUser_throwUserAuthenticationException() {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("nonExistingUser", "password");
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserAuthenticationException.class, () -> userService.authorize(requestDTO));
        Mockito.verify(auditionEventService, Mockito.times(0)).addEvent(Mockito.any());
    }

    @Test
    void testAuthorize_whenWrongPassword_throwUserAuthenticationException() {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("testUser", "wrongPassword");
        User user = User.builder().name("testUser").password("encodedPassword").build();
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(false);

        Assertions.assertThrows(UserAuthenticationException.class, () -> userService.authorize(requestDTO));
        Mockito.verify(auditionEventService, Mockito.times(0)).addEvent(Mockito.any());
    }

    @Test
    void testLogout() {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("testUser", "password");
        User user = User.builder().name("testUser").password("password").build();
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(true);

        userService.authorize(requestDTO);
        Assertions.assertEquals(user, userService.getCurrentUser());

        userService.logout();
        Assertions.assertNull(userService.getCurrentUser());

        Mockito.verify(passwordEncoder, Mockito.times(1)).verify(requestDTO.password(), user.getPassword());
        Mockito.verify(auditionEventService, Mockito.times(2)).addEvent(Mockito.any());
    }

    @Test
    void testCheckUserExistsByName() {
        String username = "existingUser";
        Mockito.when(userRepository.checkUserExistsByName(username)).thenReturn(true);

        boolean result = userService.checkUserExistsByName(username);

        Assertions.assertTrue(result);
    }

    @Test
    void testCheckUserExistsById() {
        long userId = 1L;
        Mockito.when(userRepository.checkUserExistsById(userId)).thenReturn(true);

        boolean result = userService.checkUserExistsById(userId);

        Assertions.assertTrue(result);
    }

    @Test
    void testGetCurrentUserDTO() {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("testUser", "password");
        User user = User.builder().name("testUser").password("password").build();
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.verify(requestDTO.password(), user.getPassword())).thenReturn(true);

        userService.authorize(requestDTO);

        UserDTO result = userService.getCurrentUserDTO();

        Assertions.assertEquals(user.getName(), result.name());
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User user = User.builder().name("testUser").password("password").build();
        Mockito.when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        Assertions.assertEquals(user, result);
    }
}