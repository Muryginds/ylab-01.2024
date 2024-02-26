package io.ylab.service;

import io.ylab.entity.User;
import io.ylab.exception.UserNotFoundException;
import io.ylab.mapper.UserMapper;
import io.ylab.model.UserModel;
import io.ylab.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCheckUserExistsByName_Exists() {
        String username = "test";
        when(userRepository.checkUserExistsByName(username)).thenReturn(true);

        boolean result = userService.checkUserExistsByName(username);

        assertTrue(result);
    }

    @Test
    void testCheckUserExistsByName_NotExists() {
        String username = "test";
        when(userRepository.checkUserExistsByName(username)).thenReturn(false);

        boolean result = userService.checkUserExistsByName(username);

        assertFalse(result);
    }

    @Test
    void testCheckUserExistsById_Exists() {
        Long userId = 1L;
        when(userRepository.checkUserExistsById(userId)).thenReturn(true);

        boolean result = userService.checkUserExistsById(userId);

        assertTrue(result);
    }

    @Test
    void testCheckUserExistsById_NotExists() {
        Long userId = 1L;
        when(userRepository.checkUserExistsById(userId)).thenReturn(false);

        boolean result = userService.checkUserExistsById(userId);

        assertFalse(result);
    }

    @Test
    void testGetUserById_Exists() {
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        UserModel userModel = UserModel.builder().id(userId).build();
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(userModel));
        when(userMapper.toUser(userModel)).thenReturn(user);

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    void testGetUserById_NotExists() {
        Long userId = 1L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetUserByName_Exists() {
        String userName = "test";
        User user = User.builder().name(userName).build();
        UserModel userModel = UserModel.builder().name(userName).build();
        when(userRepository.findUserByName(userName)).thenReturn(Optional.of(userModel));
        when(userMapper.toUser(userModel)).thenReturn(user);

        User result = userService.getUserByName(userName);

        assertEquals(user, result);
    }

    @Test
    void testGetUserByName_NotExists() {
        String userName = "test";
        when(userRepository.findUserByName(userName)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByName(userName));
    }
}
