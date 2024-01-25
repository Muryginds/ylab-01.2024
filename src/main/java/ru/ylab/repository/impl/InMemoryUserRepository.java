package ru.ylab.repository.impl;

import ru.ylab.entity.User;
import ru.ylab.enumerated.UserRole;
import ru.ylab.repository.UserRepository;
import ru.ylab.security.Password4jPasswordEncoder;
import ru.ylab.security.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private static final PasswordEncoder passwordEncoder = new Password4jPasswordEncoder();
    private static final Map<Long, User> USERS = init();

    private static Map<Long, User> init() {
        var admin = User.builder()
                .name("admin")
                .password(passwordEncoder.encode("admin"))
                .role(UserRole.ADMIN)
                .build();
        var testUser = User.builder()
                .name("testUser")
                .password(passwordEncoder.encode("testUser"))
                .build();
        Map<Long, User> map = new HashMap<>();
        map.put(admin.getId(), admin);
        map.put(testUser.getId(), testUser);
        return map;
    }

    @Override
    public boolean checkUserExistsByName(String username) {
        return getUserByName(username).isPresent();
    }

    @Override
    public void save(User user) {
        USERS.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUserByName(String username) {
        return USERS.values().stream()
                .filter(u -> u.getName().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(USERS.get(userId));
    }
}
