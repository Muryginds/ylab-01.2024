package ru.ylab.in.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String name
        ) {
}
