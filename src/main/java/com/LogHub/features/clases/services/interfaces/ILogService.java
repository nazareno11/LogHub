package com.LogHub.features.clases.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;


import com.LogHub.features.clases.dtos.request.LogRequestDTO;
import com.LogHub.features.clases.dtos.response.LogResponseDTO;
import com.LogHub.features.clases.entity.Application;

public interface ILogService {


    List<LogResponseDTO> getLogsByApplication(Long appId);

    List<LogResponseDTO> getLogsByApplicationAndDates(
            Long appId,
            LocalDateTime from,
            LocalDateTime to
    );
    /*validar la apikey */
    LogResponseDTO createLog(LogRequestDTO dto, Application application);

}
