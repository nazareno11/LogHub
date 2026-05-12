package com.LogHub.features.clases.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.LogHub.config.exceptions.ApplicationNotFoundException;
import com.LogHub.config.exceptions.InvalidApiKeyException;
import com.LogHub.features.clases.dtos.request.LogRequestDTO;
import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.entity.Application;
import com.LogHub.features.clases.entity.Log;
import com.LogHub.features.clases.mappers.LogMapper;
import com.LogHub.features.clases.repository.IApplicationRepository;
import com.LogHub.features.clases.repository.ILogRepository;
import com.LogHub.features.clases.services.interfaces.ILogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements ILogService {

    private final ILogRepository logRepository;
    private final IApplicationRepository applicationRepository;

    @Override
    public LogResponseDTO createLog(UUID apiKey, LogRequestDTO dto) {

        Application app = applicationRepository.findById(dto.getAppId())
                .orElseThrow(
                        () -> new ApplicationNotFoundException("Aplicación no encontrada con ID: " + dto.getAppId()));


        if (!app.getApiKey().equals(apiKey)) {
            throw new InvalidApiKeyException("La API Key provista no es válida para esta aplicación");
        } /*cambiar de lugar */

        Log log = Log.builder()
                .message(dto.getMessage())
                .logLevel(dto.getLogLevel())
                .timestamp(LocalDateTime.now()) 
                .application(app)
                .build();

        Log savedLog = logRepository.save(log);

        return LogMapper.toDTO(savedLog);
    }

    @Override
    public List<LogResponseDTO> getLogsByApplication(Long appId) {
        return logRepository.findByApplicationId(appId)
                .stream()
                .map(LogMapper::toDTO)
                .toList();
    }

    @Override
    public List<LogResponseDTO> getLogsByApplicationAndDates(Long appId,
            LocalDateTime from,
            LocalDateTime to) {
        return logRepository
                .findByApplicationIdAndTimestampBetween(appId, from, to)
                .stream()
                .map(LogMapper::toDTO)
                .toList();
    }
}
