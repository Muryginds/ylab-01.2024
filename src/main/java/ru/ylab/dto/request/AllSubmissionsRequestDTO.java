package ru.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a request to retrieve all submissions by user ID.
 *
 * <p>This class is used to transfer information about a user's request to retrieve all submissions by specifying user ID.
 */
@Builder
public record AllSubmissionsRequestDTO(
        @Positive
        @NotNull
        @JsonProperty("userId")
        Long userId
) {
}
