package io.ylab.model;

import lombok.Builder;
import io.ylab.enumerated.UserRole;

@Builder
public record UserModel(
        Long id,
        String name,
        String password,
        UserRole role) {

    public static class UserModelBuilder {
        private UserRole role = UserRole.USER;
    }
}
