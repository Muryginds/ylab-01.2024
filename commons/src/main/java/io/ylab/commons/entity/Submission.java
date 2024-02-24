package io.ylab.commons.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public final class Submission {
    private Long id;
    private User user;
    @Builder.Default
    private LocalDate date = LocalDate.now();
}
