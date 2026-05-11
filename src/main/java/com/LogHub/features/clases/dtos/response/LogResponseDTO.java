package com.LogHub.features.clases.dtos.response;

import java.time.LocalDateTime;

import com.LogHub.features.clases.entity.LogLevel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogResponseDTO {

    private Long id;
    private String message;
    private LogLevel logLevel;
    private LocalDateTime timestamp;
    private String applicationName;
}
