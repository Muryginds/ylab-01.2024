package ru.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ylab.CommonContainerBasedTest;
import ru.ylab.entity.User;
import ru.ylab.enumerated.UserRole;
import ru.ylab.model.UserModel;
import ru.ylab.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserRepositoryTest extends CommonContainerBasedTest {
    private static UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        userRepository = new JdbcUserRepository(dbConnectionFactory);
        createTestUsers();
    }

    @Test
    void testCheckUserExistsByName_whenExistingUser_thenReturnTrue() {
        assertTrue(userRepository.checkUserExistsByName("user1"));
    }

    @Test
    void testCheckUserExistsByName_whenNonExistingUser_thenReturnFalse() {
        assertFalse(userRepository.checkUserExistsByName("nonexistentUser"));
    }

    @Test
    void testCheckUserExistsById_whenExistingUser_thenReturnTrue() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertTrue(userModelOptional.isPresent());
        assertTrue(userRepository.checkUserExistsById(userModelOptional.get().id()));
    }

    @Test
    void testCheckUserExistsById_whenNonExistingUser_thenReturnFalse() {
        assertFalse(userRepository.checkUserExistsById(999L));
    }

    @Test
    void testFindUserByName_whenExistingUser_thenReturnUserModel() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertTrue(userModelOptional.isPresent());

        var userModel = userModelOptional.get();
        assertEquals("user1", userModel.name());
        assertEquals("password1", userModel.password());
        assertEquals(UserRole.USER, userModel.role());
    }

    @Test
    void testFindUserByName_whenNonExistingUser_thenReturnEmptyOptional() {
        var userModelOptional = userRepository.findUserByName("nonexistentUser");
        assertFalse(userModelOptional.isPresent());
    }

    @Test
    void testFindUserById_whenExistingUser_thenReturnUserModel() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertTrue(userModelOptional.isPresent());

        Optional<UserModel> foundUserModel = userRepository.findUserById(userModelOptional.get().id());
        assertTrue(foundUserModel.isPresent());

        assertEquals(foundUserModel.get(), userModelOptional.get());
    }

    @Test
    void testFindUserById_whenNonExistingUser_thenReturnEmptyOptional() {
        Optional<UserModel> foundUserModel = userRepository.findUserById(999L);
        assertFalse(foundUserModel.isPresent());
    }

    private static void createTestUsers() {
        userRepository.save(User.builder().name("user1").password("password1").role(UserRole.USER).build());
        userRepository.save(User.builder().name("user2").password("password2").role(UserRole.ADMIN).build());
    }
}