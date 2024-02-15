package io.ylab.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ErrorDto(
        @JsonProperty("error")
        String error
) {
}
