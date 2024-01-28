package ru.ylab.repository;

import ru.ylab.entity.User;

import java.util.Optional;

public interface UserRepository {
    boolean checkUserExistsByName(String username);

    boolean checkUserExistsById(Long userId);

    void save(User user);

    Optional<User> findUserByName(String name);

    Optional<User> findUserById(Long userId);
}
