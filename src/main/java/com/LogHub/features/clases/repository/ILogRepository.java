package com.LogHub.features.clases.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LogHub.features.clases.entity.Log;

public interface ILogRepository extends JpaRepository<Log, Long>{
    /*Filtrar por application */
    List<Log> findByApplicationId(Long applicationId);

    /*Filtro por fechas */
    List<Log> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    /*Filtrar por application y fechas */
    List<Log> findByApplicationIdAndTimestampBetween(
            Long applicationId,
            LocalDateTime from,
            LocalDateTime to
    );
}
