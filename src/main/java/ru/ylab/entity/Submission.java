package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public final class Submission {
    private static Long counter = 1L;

    @Builder.Default
    private final Long id = counter++;
    private final User user;
    @Builder.Default
    private final LocalDate date = LocalDate.now();
}
