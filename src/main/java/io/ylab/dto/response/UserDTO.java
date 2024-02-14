package io.ylab.dto.response;

import lombok.Builder;
import io.ylab.enumerated.UserRole;

/**
 * Data Transfer Object (DTO) representing a user.
 *
 * <p>This class is used to transfer user-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record UserDTO(
        Long id,
        String name,
        UserRole role
) {
}
