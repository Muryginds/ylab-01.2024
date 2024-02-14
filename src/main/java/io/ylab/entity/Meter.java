package io.ylab.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class Meter {
    private Long id;
    private String factoryNumber;
    private User user;
    private MeterType meterType;
}
