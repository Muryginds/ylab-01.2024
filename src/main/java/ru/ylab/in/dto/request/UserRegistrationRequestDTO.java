package ru.ylab.in.dto.request;

import lombok.Builder;

@Builder
public record UserRegistrationRequestDTO(
        String name,
        String password) {
}
