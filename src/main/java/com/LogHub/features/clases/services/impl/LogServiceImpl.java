package com.LogHub.features.clases.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LogHub.config.exceptions.ApplicationNotFoundException;
import com.LogHub.config.web.RequestContext;
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
    private final LogMapper logMapper;

    @Override
    @Transactional
    public LogResponseDTO createLog(LogRequestDTO dto) {

        Application application = RequestContext.getApplication();

        if (application == null) {
            throw new ApplicationNotFoundException(
                    "No se encontró la aplicación asociada a la petición");
        }

        if (!application.getId().equals(dto.getAppId())) {
            throw new IllegalArgumentException(
                    "La aplicación de la API Key no coincide con el appId enviado");
        }

        Log log = logMapper.toEntity(dto, application);

        log.setClientIp(RequestContext.getClientIp());
        log.setHttpMethod(RequestContext.getHttpMethod());
        log.setEndpoint(RequestContext.getEndpoint());

        log.setStatusCode(RequestContext.getStatusCode());
        log.setDurationMs(RequestContext.getDurationMs());

        Log savedLog = logRepository.save(log);

        return logMapper.toResponseDTO(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponseDTO> getLogsByApplication(Long appId) {

        if (!applicationRepository.existsById(appId)) {
            throw new ApplicationNotFoundException(
                    "No se encontró la aplicación con id: " + appId);
        }

        return logRepository.findByApplicationId(appId)
                .stream()
                .map(logMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogResponseDTO> getLogsByApplicationAndDates(
            Long appId,
            java.time.LocalDateTime from,
            java.time.LocalDateTime to) {

        if (!applicationRepository.existsById(appId)) {
            throw new ApplicationNotFoundException(
                    "No se encontró la aplicación con id: " + appId);
        }

        return logRepository
                .findByApplicationIdAndTimestampBetween(appId, from, to)
                .stream()
                .map(logMapper::toResponseDTO)
                .toList();
    }
}
