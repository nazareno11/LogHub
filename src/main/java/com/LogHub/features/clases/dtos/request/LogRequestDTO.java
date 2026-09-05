package com.LogHub.features.clases.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogRequestDTO {

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @NotNull(message = "El nivel del log es obligatorio")
    private com.LogHub.features.clases.entity.LogLevel logLevel;

    @NotNull(message = "El appId es obligatorio")
    private Long appId;

    @NotNull(message = "El código de estado es obligatorio")
    private Integer statusCode;

    @NotNull(message = "La duración es obligatoria")
    private Long durationMs;
}

