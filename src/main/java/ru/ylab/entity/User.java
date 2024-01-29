package ru.ylab.entity;

import lombok.*;
import ru.ylab.enumerated.UserRole;

@Builder
@Data
public final class User {
    private static Long counter = 1L;

    @Builder.Default
    private final Long id = counter++;
    private final String name;
    private final String password;
    @Builder.Default
    private final UserRole role = UserRole.USER;
}
