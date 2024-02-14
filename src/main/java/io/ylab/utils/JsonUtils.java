package io.ylab.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import io.ylab.exception.DtoValidationException;

import java.io.BufferedReader;
import java.io.IOException;

@UtilityClass
public class JsonUtils {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public <T> T readJson(BufferedReader reader, Class<T> tClass) {
        try {
            return objectMapper.readValue(reader, tClass);
        } catch (IOException e) {
            throw new DtoValidationException(e.getMessage());
        }
    }

    public byte[] writeJsonAsBytes(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
    }
}
