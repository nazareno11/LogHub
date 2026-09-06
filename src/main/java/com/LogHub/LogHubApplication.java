package com.LogHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.LogHub.config.EnvLoader;

@SpringBootApplication
public class LogHubApplication {
    public static void main(String[] args) {
        EnvLoader.load();
        SpringApplication.run(LogHubApplication.class, args);
    }
}
