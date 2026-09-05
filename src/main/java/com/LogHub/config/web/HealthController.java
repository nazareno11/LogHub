package com.LogHub.config.web;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(
                "status", "UP",
                "service", "LogHub",
                "timestamp", LocalDateTime.now()
        );
    }
}
