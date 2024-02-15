package io.ylab.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ReadingRequestDto(
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
