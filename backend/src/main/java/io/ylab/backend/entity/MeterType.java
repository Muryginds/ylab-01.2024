package io.ylab.backend.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class MeterType {
    private Long id;
    private String typeName;
}
