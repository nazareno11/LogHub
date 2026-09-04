package com.LogHub.config.web;

import com.LogHub.features.clases.entity.Application;

public final class RequestContext {

    private static final ThreadLocal<Application> APPLICATION = new ThreadLocal<>();
    private static final ThreadLocal<String> CLIENT_IP = new ThreadLocal<>();
    private static final ThreadLocal<String> HTTP_METHOD = new ThreadLocal<>();
    private static final ThreadLocal<String> ENDPOINT = new ThreadLocal<>();
    private static final ThreadLocal<Integer> STATUS_CODE = new ThreadLocal<>();
    private static final ThreadLocal<Long> DURATION_MS = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void setApplication(Application application) {
        APPLICATION.set(application);
    }

    public static Application getApplication() {
        return APPLICATION.get();
    }

    public static void setClientIp(String clientIp) {
        CLIENT_IP.set(clientIp);
    }

    public static String getClientIp() {
        return CLIENT_IP.get();
    }

    public static void setHttpMethod(String httpMethod) {
        HTTP_METHOD.set(httpMethod);
    }

    public static String getHttpMethod() {
        return HTTP_METHOD.get();
    }

    public static void setEndpoint(String endpoint) {
        ENDPOINT.set(endpoint);
    }

    public static String getEndpoint() {
        return ENDPOINT.get();
    }

    public static void setStatusCode(Integer statusCode) {
        STATUS_CODE.set(statusCode);
    }

    public static Integer getStatusCode() {
        return STATUS_CODE.get();
    }

    public static void setDurationMs(Long durationMs) {
        DURATION_MS.set(durationMs);
    }

    public static Long getDurationMs() {
        return DURATION_MS.get();
    }

    public static void clear() {
        APPLICATION.remove();
        CLIENT_IP.remove();
        HTTP_METHOD.remove();
        ENDPOINT.remove();
        STATUS_CODE.remove();
        DURATION_MS.remove();
    }
}
