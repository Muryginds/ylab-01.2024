package ru.ylab.dto;

import lombok.Builder;

@Builder
public record UserRegistrationRequestDTO(
        String name,
        String password) {
}
