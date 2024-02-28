package io.ylab.backend.model;

import io.ylab.commons.enumerated.UserRole;
import lombok.Builder;

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
