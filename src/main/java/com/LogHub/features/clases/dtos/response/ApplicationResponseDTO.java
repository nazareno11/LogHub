package com.LogHub.features.clases.dtos.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String email;
    private UUID apiKey;
}
