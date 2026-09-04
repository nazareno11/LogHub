package com.LogHub.features.clases.dtos.response;

import com.LogHub.features.clases.entity.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogResponseDTO {

    private Long id;

    private String message;

    private LogLevel logLevel;

    private LocalDateTime timestamp;

    private String applicationName;

    private String clientIp;

    private String httpMethod;

    private String endpoint;

    private Integer statusCode;

    private Long durationMs;
}
