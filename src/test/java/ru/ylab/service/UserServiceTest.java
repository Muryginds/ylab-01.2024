package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.mapper.UserMapper;
import ru.ylab.model.UserModel;
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
    void testCheckUserExistsByName() {
        var username = "existingUser";
        Mockito.when(userRepository.checkUserExistsByName(username)).thenReturn(true);

        var result = userService.checkUserExistsByName(username);

        Assertions.assertTrue(result);
    }

    @Test
    void testCheckUserExistsById() {
        var userId = 1L;
        Mockito.when(userRepository.checkUserExistsById(userId)).thenReturn(true);

        var result = userService.checkUserExistsById(userId);

        Assertions.assertTrue(result);
    }

    @Test
    void testGetCurrentUserDTO() {
        var requestDTO = new UserAuthorizationRequestDTO("testUser", "password");
        var userModel = UserModel.builder().name("testUser").password("password").build();
        var user = UserMapper.MAPPER.toUser(userModel);
        Mockito.when(userRepository.findUserByName(requestDTO.name())).thenReturn(Optional.of(userModel));
        Mockito.when(passwordEncoder.verify(requestDTO.password(), userModel.password())).thenReturn(true);

        userService.setCurrentUser(user);

        var result = userService.getCurrentUserDTO();

        Assertions.assertEquals(userModel.name(), result.name());
    }

    @Test
    void testGetUserById() {
        var userId = 1L;
        var userModel = UserModel.builder().id(userId).name("testUser").password("password").build();
        var expectedUser = UserMapper.MAPPER.toUser(userModel);
        Mockito.when(userRepository.findUserById(userId)).thenReturn(Optional.of(userModel));

        var userResult = userService.getUserById(userId);

        Assertions.assertEquals(expectedUser, userResult);
    }
}