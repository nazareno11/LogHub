package com.LogHub.features.clases.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LogHub.config.exceptions.EmailAlreadyRegisteredException;
import com.LogHub.features.clases.dtos.request.ApplicationRequestDTO;
import com.LogHub.features.clases.dtos.response.ApplicationListResponseDTO;
import com.LogHub.features.clases.dtos.response.ApplicationResponseDTO;
import com.LogHub.features.clases.mappers.ApplicationMapper;
import com.LogHub.features.clases.repository.IApplicationRepository;
import com.LogHub.features.clases.services.interfaces.IApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements IApplicationService {

    private final IApplicationRepository applicationRepository;

    @Override
    public ApplicationResponseDTO registerApplication(ApplicationRequestDTO dto) {

        if (applicationRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyRegisteredException(
                    "El email ya está registrado");
        }

        var application = ApplicationMapper.toEntity(dto);
        var savedApp = applicationRepository.save(application);

        return ApplicationMapper.toResponseDTO(savedApp);
    }

    @Override
    public List<ApplicationListResponseDTO> getAllApplications() {

        return applicationRepository.findAll()
                .stream()
                .map(application -> ApplicationListResponseDTO.builder()
                        .id(application.getId())
                        .name(application.getName())
                        .description(application.getDescription())
                        .email(application.getEmail())
                        .build())
                .toList();
    }
}
