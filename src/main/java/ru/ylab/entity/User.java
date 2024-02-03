package ru.ylab.entity;

import lombok.*;
import ru.ylab.enumerated.UserRole;

@Builder
@Data
public final class User {
    private Long id;
    private String name;
    private String password;
    @Builder.Default
    private UserRole role = UserRole.USER;
}
