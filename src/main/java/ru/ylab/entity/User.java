package ru.ylab.entity;

import lombok.Builder;

@Builder
public record User(
        Long id,
        String name,
        String password,
        boolean isAdmin) {
    public static class UserBuilder {
        private static Long userCounter = 0L;
        private Long id = userCounter++;
    }
}
