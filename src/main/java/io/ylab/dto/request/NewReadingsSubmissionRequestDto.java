package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a submission request.
 *
 * <p>This class is used to transfer information about a user's meter readings submission from the client to the server.
 */
@Builder
public record NewReadingsSubmissionRequestDto(
        @NotEmpty
        @JsonProperty("meterReadings")
        List<ReadingRequestDto> meterReadings
) {
}
