package com.LogHub.features.clases.mappers;

import org.springframework.stereotype.Component;

import com.LogHub.features.clases.dtos.request.LogRequestDTO;
import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.entity.Application;
import com.LogHub.features.clases.entity.Log;

@Component
public class LogMapper {

    public Log toEntity(LogRequestDTO dto, Application application) {
        return Log.builder()
                .message(dto.getMessage())
                .logLevel(dto.getLogLevel())
                .application(application)
                .build();
    }

    public LogResponseDTO toResponseDTO(Log log) {
        return LogResponseDTO.builder()
                .id(log.getId())
                .message(log.getMessage())
                .logLevel(log.getLogLevel())
                .timestamp(log.getTimestamp())
                .applicationName(
                        log.getApplication() != null
                                ? log.getApplication().getName()
                                : null)
                .clientIp(log.getClientIp())
                .httpMethod(log.getHttpMethod())
                .endpoint(log.getEndpoint())
                .statusCode(log.getStatusCode())
                .durationMs(log.getDurationMs())
                .build();
    }
}
