package com.LogHub.features.clases.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LogHub.config.exceptions.InvalidApiKeyException;
import com.LogHub.config.web.RequestContext;
import com.LogHub.features.clases.dtos.request.LogRequestDTO;
import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.entity.Application;
import com.LogHub.features.clases.entity.Log;
import com.LogHub.features.clases.mappers.LogMapper;
import com.LogHub.features.clases.repository.ILogRepository;
import com.LogHub.features.clases.services.interfaces.ILogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements ILogService {

    private final ILogRepository logRepository;

    @Override
    public LogResponseDTO createLog(LogRequestDTO dto) {

        Application app = RequestContext.getApplication();
        String ip = RequestContext.getIp();
        String method = RequestContext.getMethod();
        String uri = RequestContext.getEndpoint();

        if (!app.getId().equals(dto.getAppId())) {
            throw new InvalidApiKeyException("La API Key no pertenece al appId indicado");
        }

        Log log = Log.builder()
                .message(dto.getMessage())
                .logLevel(dto.getLogLevel())
                .timestamp(LocalDateTime.now())
                .application(app)
                .clienteIp(ip)
                .httpMetod(method)
                .endpoint(uri)
                .build();

        return LogMapper.toDTO(logRepository.save(log));
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
