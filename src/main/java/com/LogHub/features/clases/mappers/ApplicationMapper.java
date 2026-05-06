package com.LogHub.features.clases.mappers;

import com.LogHub.features.clases.dtos.request.ApplicationRequestDTO;
import com.LogHub.features.clases.dtos.response.ApplicationResponseDTO;
import com.LogHub.features.clases.entity.Application;

public class ApplicationMapper {

    private ApplicationMapper() {}

    public static Application toEntity(ApplicationRequestDTO dto) {
        return Application.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .email(dto.getEmail())
                .build();
    }

    public static ApplicationResponseDTO toResponseDTO(Application app) {
        return ApplicationResponseDTO.builder()
                .id(app.getId())
                .name(app.getName())
                .description(app.getDescription())
                .email(app.getEmail())
                .apiKey(app.getApiKey())
                .build();
    }
    
}
