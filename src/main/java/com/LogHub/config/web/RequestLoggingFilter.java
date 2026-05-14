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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String ip = request.getRemoteAddr();
            String method = request.getMethod();
            String uri = request.getRequestURI();

            RequestContext.setRequestData(ip, method, uri);

            filterChain.doFilter(request, response);

        } finally {
            RequestContext.clear();
        }
    }
}
