package com.LogHub.features.clases.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.LogHub.features.clases.dtos.request.ApplicationRequestDTO;
import com.LogHub.features.clases.dtos.response.ApplicationListResponseDTO;
import com.LogHub.features.clases.dtos.response.ApplicationResponseDTO;
import com.LogHub.features.clases.services.interfaces.IApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Tag(
    name = "Applications",
    description = "Gestión de aplicaciones cliente que envían logs"
)
public class ApplicationController {

    private final IApplicationService applicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registrar una nueva aplicación",
        description = "Registra una aplicación cliente y genera automáticamente su API Key"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Aplicación registrada correctamente"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de validación o email duplicado"
    )
    public ApplicationResponseDTO registerApplication(
            @Valid @RequestBody ApplicationRequestDTO requestDTO) {

        return applicationService.registerApplication(requestDTO);
    }

    @GetMapping
    @Operation(
        summary = "Listar aplicaciones registradas",
        description = "Devuelve todas las aplicaciones sin exponer sus API Keys"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Listado obtenido correctamente"
    )
    public List<ApplicationListResponseDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }
}
