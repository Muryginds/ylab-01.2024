package ru.ylab.model;

import lombok.Builder;
import ru.ylab.enumerated.UserRole;

@Builder
public record UserModel(
        Long id,
        String name,
        String password,
        UserRole role) {

    public static class UserModelBuilder {
        private final UserRole role = UserRole.USER;
    }
}
