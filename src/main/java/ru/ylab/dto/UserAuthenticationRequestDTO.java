package ru.ylab.dto;

import lombok.Builder;

@Builder
public record UserAuthenticationRequestDTO(
        String name,
        String password) {
}
