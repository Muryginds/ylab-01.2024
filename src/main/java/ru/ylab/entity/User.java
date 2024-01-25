package ru.ylab.entity;

import lombok.*;

@Builder
@Data
public class User {
    private static Long userCounter = 0L;

    @Builder.Default
    private final Long id = userCounter++;
    private final String name;
    private final String password;
    @Builder.Default
    private final boolean isAdmin = false;
}
