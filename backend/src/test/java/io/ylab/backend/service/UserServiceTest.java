package io.ylab.backend.service;

import io.ylab.commons.entity.User;
import io.ylab.backend.exception.UserNotFoundException;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.model.UserModel;
import io.ylab.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        var username = "test";
        when(userRepository.checkUserExistsByName(username)).thenReturn(true);

        boolean result = userService.checkUserExistsByName(username);

        assertTrue(result);
    }

    @Test
    void testCheckUserExistsByName_NotExists() {
        var username = "test";
        when(userRepository.checkUserExistsByName(username)).thenReturn(false);

        boolean result = userService.checkUserExistsByName(username);

        assertFalse(result);
    }

    @Test
    void testCheckUserExistsById_Exists() {
        var userId = 1L;
        when(userRepository.checkUserExistsById(userId)).thenReturn(true);

        boolean result = userService.checkUserExistsById(userId);

        assertTrue(result);
    }

    @Test
    void testCheckUserExistsById_NotExists() {
        var userId = 1L;
        when(userRepository.checkUserExistsById(userId)).thenReturn(false);

        boolean result = userService.checkUserExistsById(userId);

        assertFalse(result);
    }

    @Test
    void testGetUserById_Exists() {
        var userId = 1L;
        var user = User.builder().id(userId).build();
        var userModel = UserModel.builder().id(userId).build();
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(userModel));
        when(userMapper.toUser(userModel)).thenReturn(user);

        var result = userService.getUserById(userId);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void testGetUserById_NotExists() {
        var userId = 1L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetUserByName_Exists() {
        var userName = "test";
        var user = User.builder().name(userName).build();
        var userModel = UserModel.builder().name(userName).build();
        when(userRepository.findUserByName(userName)).thenReturn(Optional.of(userModel));
        when(userMapper.toUser(userModel)).thenReturn(user);

        var result = userService.getUserByName(userName);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void testGetUserByName_NotExists() {
        var userName = "test";
        when(userRepository.findUserByName(userName)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByName(userName));
    }
}
