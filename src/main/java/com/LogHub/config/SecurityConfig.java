package com.LogHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.LogHub.config.security.ApiKeyFilter;
import com.LogHub.config.web.RequestLoggingFilter;
import com.LogHub.features.clases.repository.IApplicationRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RequestLoggingFilter requestLoggingFilter;
    private final IApplicationRepository applicationRepository;

    @Bean
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(applicationRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .addFilterBefore(
                requestLoggingFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .addFilterAfter(
                apiKeyFilter(),
                RequestLoggingFilter.class
            )

            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
