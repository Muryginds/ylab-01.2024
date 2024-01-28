package ru.ylab.in.dto.request;

import lombok.Builder;

@Builder
public record UserAuthorizationRequestDTO(
        String name,
        String password
) {
}
