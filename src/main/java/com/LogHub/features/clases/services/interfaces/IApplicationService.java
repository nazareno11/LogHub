package com.LogHub.features.clases.services.interfaces;

import java.util.List;

import com.LogHub.features.clases.dtos.request.ApplicationRequestDTO;
import com.LogHub.features.clases.dtos.response.ApplicationListResponseDTO;
import com.LogHub.features.clases.dtos.response.ApplicationResponseDTO;

public interface IApplicationService {

    ApplicationResponseDTO registerApplication(ApplicationRequestDTO dto);

    List<ApplicationListResponseDTO> getAllApplications();
}
