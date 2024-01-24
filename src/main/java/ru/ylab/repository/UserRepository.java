package ru.ylab.repository;

import ru.ylab.entity.User;

import java.util.Optional;

public interface UserRepository {
    boolean checkUserExistsByName(String username);

    void save(User user);

    Optional<User> getUserByName(String name);
}
