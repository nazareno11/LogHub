package com.LogHub.features.clases.controllers;

import com.LogHub.features.clases.dtos.request.LogRequestDTO;
import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.services.interfaces.ILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "Endpoints para registrar y consultar eventos de monitoreo")
public class LogController {

    private final ILogService logService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar un nuevo log de evento",
            description = "Recibe un evento de una app externa. Requiere validación estricta de API Key en la cabecera."
    )
    @ApiResponse(responseCode = "201", description = "Log guardado correctamente")
    @ApiResponse(responseCode = "401", description = "API Key inválida o no pertenece a la aplicación")
    @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos")
    public LogResponseDTO createLog(
            @RequestHeader("X-API-KEY") UUID apiKey, 
            @Valid @RequestBody LogRequestDTO dto    
    ) {
        
        return logService.createLog(apiKey, dto); 
    }

  
    @GetMapping("/application/{appId}")
    @Operation(summary = "Listar logs de una aplicación", description = "Devuelve el historial completo de eventos de una app")
    @ApiResponse(responseCode = "200", description = "Listado obtenido con éxito")
    public List<LogResponseDTO> getLogsByApplication(@PathVariable Long appId) {
        return logService.getLogsByApplication(appId);
    }


    @GetMapping("/application/{appId}/dates")
    @Operation(summary = "Filtrar logs por rango de fechas", description = "Busca logs de una app específica entre dos marcas de tiempo")
    @ApiResponse(responseCode = "200", description = "Filtro aplicado con éxito")
    public List<LogResponseDTO> getLogsByApplicationAndDates(
            @PathVariable Long appId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return logService.getLogsByApplicationAndDates(appId, from, to);
    }
}
