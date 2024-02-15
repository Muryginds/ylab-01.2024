package ru.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ReadingRequestDTO(
        @Positive
        @NotEmpty
        @JsonProperty("meterId")
        Long meterId,
        @Positive
        @NotEmpty
        @JsonProperty("value")
        Long value
) {
}
