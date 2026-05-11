package com.LogHub.features.clases.dtos.request;

import com.LogHub.features.clases.entity.LogLevel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogRequestDTO {

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Descripción del evento", example = "Intento de login fallido")
    private String message;

    @NotNull(message = "El nivel de log es obligatorio")
    @Schema(description = "Nivel de severidad", example = "WARNING")
    private LogLevel logLevel;

    @NotNull(message = "El ID de la aplicación es obligatorio")
    @Schema(description = "ID de la app que genera el log", example = "1")
    private Long appId;
}
