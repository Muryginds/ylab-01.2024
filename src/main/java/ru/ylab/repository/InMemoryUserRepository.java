package ru.ylab.repository;

import ru.ylab.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private static final Map<String, User> USERS = init();

    private static Map<String, User> init() {
        User admin = User.builder()
                .name("admin")
                .password("password")
                .isAdmin(true)
                .build();
        User testUser = User.builder()
                .name("testUser")
                .password("password2")
                .isAdmin(false)
                .build();
        Map<String, User> map = new HashMap<>();
        map.put(admin.name(), admin);
        map.put(testUser.name(), testUser);
        return map;
    }

    @Override
    public boolean checkUserExistsByName(String username) {
        return USERS.containsKey(username);
    }

    @Override
    public void save(User user) {
        USERS.put(user.name(), user);
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return Optional.ofNullable(USERS.get(name));
    }
}
