package ru.ylab.out.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String name
        ) {
}
