package com.LogHub.features.clases.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LogHub.features.clases.dtos.request.ApplicationRequestDTO;
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
            throw new RuntimeException("El email ya está registrado"); 
        }
        //EmailAlreadyExistsException

        var application = ApplicationMapper.toEntity(dto);
        var savedApp = applicationRepository.save(application);
        return ApplicationMapper.toResponseDTO(savedApp);
    }

    @Override
    public List<ApplicationResponseDTO> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(ApplicationMapper::toResponseDTO)
                .toList();
    }
}