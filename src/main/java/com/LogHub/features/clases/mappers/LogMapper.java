package com.LogHub.features.clases.mappers;

import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.entity.Log;

public class LogMapper {

    public static LogResponseDTO toDTO(Log log) {
        return LogResponseDTO.builder()
                .id(log.getId())
                .message(log.getMessage())
                .logLevel(log.getLogLevel())
                .timestamp(log.getTimestamp())
                .applicationName(log.getApplication().getName())
                .build();
    }
}
