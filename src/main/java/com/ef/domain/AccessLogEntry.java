package com.ef.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Data
@Builder
public class AccessLogEntry {

    private LocalDateTime timestamp;
    private String ipaddress;
    private String httpCall;
    private int httpStatusCode;
    private String userAgent;

    @Tolerate
    public AccessLogEntry() {
    }
}
