package io.ylab.backend.utils;

import io.ylab.backend.dto.response.ErrorResponseDto;
import io.ylab.backend.dto.response.MessageDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResponseUtils {
    public MessageDto responseWithMessage(String message) {
        return MessageDto.builder().message(message).build();
    }

    public ErrorResponseDto responseWithError(List<String> errors) {
        return ErrorResponseDto.builder().errors(errors).build();
    }
}
