package com.LogHub.features.clases.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ApplicationRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre único del sistema cliente", example = "Sistema de Ventas")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ. ]+$", message = "El nombre solo puede contener letras, números y espacios")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;
}
