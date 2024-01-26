package ru.ylab.in.dto.request;

import lombok.Builder;

@Builder
public record UserAuthenticationRequestDTO(
        String name,
        String password) {
}
