package ru.ylab.in.dto.request;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a user authorization request.
 *
 * <p>This class is used to transfer user authorization-related information from the client to the server.
 */
@Builder
public record UserAuthorizationRequestDTO(
        String name,
        String password
) {
}
