package com.LogHub.features.clases.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


import com.LogHub.features.clases.entity.Application;

public interface IApplicationRepository extends JpaRepository <Application, Long> {
    Optional <Application> findByApikey (UUID apikey);
    boolean existsByEmail (String email);
}
