package com.LogHub.config.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.LogHub.config.web.RequestContext;
import com.LogHub.features.clases.entity.Application;
import com.LogHub.features.clases.repository.IApplicationRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String ADMIN_API_KEY_HEADER = "X-ADMIN-API-KEY";

    private final IApplicationRepository applicationRepository;

    @org.springframework.beans.factory.annotation.Value("${LOGHUB_ADMIN_API_KEY:}")
    private String adminApiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Endpoints públicos
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Endpoints administrativos
        if (uri.equals("/applications")) {
            if (!isValidAdminApiKey(request)) {
                response.sendError(
                        HttpStatus.UNAUTHORIZED.value(),
                        "API Key administrativa requerida"
                );
                return;
            }

            filterChain.doFilter(request, response);
            return;
        }

        // Resto de endpoints: API Key de una aplicación
        String apiKeyHeader = request.getHeader(API_KEY_HEADER);

        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            response.sendError(
                    HttpStatus.UNAUTHORIZED.value(),
                    "API Key requerida"
            );
            return;
        }

        UUID apiKey;

        try {
            apiKey = UUID.fromString(apiKeyHeader);
        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpStatus.UNAUTHORIZED.value(),
                    "API Key inválida"
            );
            return;
        }

        Application application = applicationRepository
                .findByApiKey(apiKey)
                .orElse(null);

        if (application == null) {
            response.sendError(
                    HttpStatus.UNAUTHORIZED.value(),
                    "API Key inválida"
            );
            return;
        }

        RequestContext.setApplication(application);
        RequestContext.setClientIp(getClientIp(request));
        RequestContext.setHttpMethod(request.getMethod());
        RequestContext.setEndpoint(request.getRequestURI());

        request.setAttribute("application", application);

        filterChain.doFilter(request, response);
    }

    private boolean isValidAdminApiKey(HttpServletRequest request) {

        String providedKey = request.getHeader(ADMIN_API_KEY_HEADER);

        return adminApiKey != null
                && !adminApiKey.isBlank()
                && adminApiKey.equals(providedKey);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {

        String uri = request.getRequestURI();

        return uri.equals("/health")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs");
    }

    private String getClientIp(HttpServletRequest request) {

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
