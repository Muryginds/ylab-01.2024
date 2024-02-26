package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a user authorization request.
 *
 * <p>This class is used to transfer user authorization-related information from the client to the server.
 */
@Builder
@Schema(description = "Модель данных запроса на авторизацию")
public record UserAuthorizationRequestDto(
        @NotBlank
        @JsonProperty("name")
        String name,
        @NotBlank
        @JsonProperty("password")
        String password
) {
}
