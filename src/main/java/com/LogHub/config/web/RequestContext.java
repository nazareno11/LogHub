package com.LogHub.config.web;

import com.LogHub.features.clases.entity.Application;

public final class RequestContext {

    private static final ThreadLocal<Application> APPLICATION = new ThreadLocal<>();
    private static final ThreadLocal<String> CLIENT_IP = new ThreadLocal<>();
    private static final ThreadLocal<String> HTTP_METHOD = new ThreadLocal<>();
    private static final ThreadLocal<String> ENDPOINT = new ThreadLocal<>();

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

    public static void setStatusCode(int statusCode) {
        // No almacenamos statusCode en ThreadLocal por ahora, se usa directamente en el filter
    }

    public static int getStatusCode() {
        return 0;
    }

    public static void setDurationMs(long durationMs) {
        // No almacenamos durationMs en ThreadLocal por ahora, se usa directamente en el filter
    }

    public static long getDurationMs() {
        return 0;
    }

    public static void clear() {
        APPLICATION.remove();
        CLIENT_IP.remove();
        HTTP_METHOD.remove();
        ENDPOINT.remove();
    }
}
