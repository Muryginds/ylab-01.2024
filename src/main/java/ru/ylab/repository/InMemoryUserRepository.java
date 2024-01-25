package ru.ylab.repository;

import ru.ylab.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private static final Map<Long, User> USERS = init();

    private static Map<Long, User> init() {
        User admin = User.builder()
                .name("admin")
                .password("password")
                .isAdmin(true)
                .build();
        User testUser = User.builder()
                .name("testUser")
                .password("password2")
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
