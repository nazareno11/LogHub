package com.LogHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.LogHub.config.EnvLoader;

@SpringBootApplication
public class LogHubApplication {
	
	static {
        new EnvLoader();
    }

	public static void main(String[] args) {
		SpringApplication.run(LogHubApplication.class, args);
	}

}
