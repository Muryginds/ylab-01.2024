package ru.ylab.in.dto;

import lombok.Builder;
import ru.ylab.enumerated.UserRole;

@Builder
public record UserDTO(
        Long id,
        String name,
        UserRole role
) {
}
