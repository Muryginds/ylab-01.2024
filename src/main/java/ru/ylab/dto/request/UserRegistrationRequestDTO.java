package ru.ylab.dto.request;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a user registration request.
 *
 * <p>This class is used to transfer user registration-related information from the client to the server.
 */
@Builder
public record UserRegistrationRequestDTO(
        String name,
        String password
) {
}
