package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a user registration request.
 *
 * <p>This class is used to transfer user registration-related information from the client to the server.
 */
@Builder
public record UserRegistrationRequestDTO(
        @NotBlank
        @JsonProperty("name")
        String name,
        @NotBlank
        @JsonProperty("password")
        String password
) {
}
