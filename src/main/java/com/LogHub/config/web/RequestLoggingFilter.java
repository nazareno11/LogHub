package com.LogHub.config.web;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);

        } finally {

            long durationMs = System.currentTimeMillis() - startTime;

            RequestContext.setStatusCode(response.getStatus());
            RequestContext.setDurationMs(durationMs);
        }
    }
}
