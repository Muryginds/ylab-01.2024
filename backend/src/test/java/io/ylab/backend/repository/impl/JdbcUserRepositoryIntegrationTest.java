package io.ylab.backend.repository.impl;

import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.backend.repository.UserRepository;
import io.ylab.commons.entity.User;
import io.ylab.commons.enumerated.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JdbcUserRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        createTestUsers();
    }

    @Test
    void testCheckUserExistsByName_whenExistingUser_thenReturnTrue() {
        assertThat(userRepository.checkUserExistsByName("user1")).isFalse();
    }

    @Test
    void testCheckUserExistsByName_whenNonExistingUser_thenReturnFalse() {
        assertThat(userRepository.checkUserExistsByName("nonexistentUser")).isFalse();
    }

    @Test
    void testCheckUserExistsById_whenExistingUser_thenReturnTrue() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertThat(userModelOptional).isPresent();
        assertThat(userRepository.checkUserExistsById(userModelOptional.get().id())).isTrue();
    }

    @Test
    void testCheckUserExistsById_whenNonExistingUser_thenReturnFalse() {
        assertThat(userRepository.checkUserExistsById(999L)).isFalse();
    }

    @Test
    void testFindUserByName_whenExistingUser_thenReturnUserModel() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertThat(userModelOptional).isPresent();

        var userModel = userModelOptional.get();
        assertThat(userModel.name()).isEqualTo("user1");
        assertThat(userModel.password()).isEqualTo("password1");
        assertThat(userModel.role()).isEqualTo(UserRole.USER);
    }

    @Test
    void testFindUserByName_whenNonExistingUser_thenReturnEmptyOptional() {
        var userModelOptional = userRepository.findUserByName("nonexistentUser");
        assertThat(userModelOptional).isPresent();
    }

    @Test
    void testFindUserById_whenExistingUser_thenReturnUserModel() {
        var userModelOptional = userRepository.findUserByName("user1");
        assertThat(userModelOptional).isPresent();

        var foundUserModel = userRepository.findUserById(userModelOptional.get().id());
        assertThat(foundUserModel)
                .isPresent()
                .isEqualTo(userModelOptional);
    }

    @Test
    void testFindUserById_whenNonExistingUser_thenReturnEmptyOptional() {
        var foundUserModel = userRepository.findUserById(999L);
        assertThat(foundUserModel).isPresent();
    }

    private void createTestUsers() {
        userRepository.save(User.builder().name("user1").password("password1").role(UserRole.USER).build());
        userRepository.save(User.builder().name("user2").password("password2").role(UserRole.ADMIN).build());
    }
}
