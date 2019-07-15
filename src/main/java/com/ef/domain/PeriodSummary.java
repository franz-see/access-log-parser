package com.ef.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Data
@Builder
public class PeriodSummary {

    private String ipaddress;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    private Long count;
    private String comment;

    @Tolerate
    public PeriodSummary() {
    }
}
