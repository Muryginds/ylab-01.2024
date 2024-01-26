package ru.ylab.in.dto;

import lombok.Builder;
import ru.ylab.enumerated.MeterType;

import java.time.LocalDate;
import java.util.Map;

@Builder
public record MeterReadingsDTO(
        Long id,
        UserDTO user,
        LocalDate date,
        Map<MeterType, Integer> readings) {
}
