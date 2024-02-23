package io.ylab.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a submission request.
 *
 * <p>This class is used to transfer information about a user's meter readings submission from the client to the server.
 */
@Builder
@Schema(description = "Модель данных подачи показаний")
public record NewReadingsSubmissionRequestDto(
        @NotEmpty
        @JsonProperty("meterReadings")
        List<ReadingRequestDto> meterReadings
) {
}
