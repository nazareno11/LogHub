package com.LogHub.features.clases.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationListResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String email;
}
