package com.LogHub.config.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.filter.OncePerRequestFilter;

import com.LogHub.config.web.RequestContext;
import com.LogHub.features.clases.entity.Application;
import com.LogHub.features.clases.repository.IApplicationRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final IApplicationRepository applicationRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String apiKeyHeader = request.getHeader("X-API-KEY");

        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Key");
            return;
        }

        try {
            UUID apiKey = UUID.fromString(apiKeyHeader);

            Application app = applicationRepository.findByApiKey(apiKey)
                    .orElse(null);

            if (app == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid API Key");
                return;
            }

            RequestContext.setApplication(app);
            RequestContext.setIp(request.getRemoteAddr());
            RequestContext.setMethod(request.getMethod());
            RequestContext.setEndpoint(request.getRequestURI());

            request.setAttribute("application", app);       

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key format");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // No filtrar el registro de apps ni Swagger/OpenAPI ni las consultas GET
        return path.startsWith("/applications") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                request.getMethod().equalsIgnoreCase("GET");
    }
}
