package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a request to retrieve a submission by date and user ID.
 *
 * <p>This class is used to transfer information about a user's request to retrieve a submission by specifying a date and user ID.
 */
@Builder
public record SubmissionRequestDto(
        @JsonProperty("date")
        LocalDate date,
        @JsonProperty("userId")
        Long userId
) {
}
