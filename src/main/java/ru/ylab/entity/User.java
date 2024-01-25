package ru.ylab.entity;

import lombok.*;
import ru.ylab.enumerated.UserRole;

@Builder
@Data
public class User {
    private static Long userCounter = 0L;

    @Builder.Default
    private final Long id = userCounter++;
    private final String name;
    private final String password;
    @Builder.Default
    private final UserRole role = UserRole.USER;
}
