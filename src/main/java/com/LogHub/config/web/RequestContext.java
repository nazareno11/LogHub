package com.LogHub.config.web;



import com.LogHub.features.clases.entity.Application;

/*guarda datos del request actual para que luego el ExceptionHandler los use*/
public class RequestContext {

    private static final ThreadLocal<Application> applicationHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> ipHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> methodHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> endpointHolder = new ThreadLocal<>();

    public static void setApplication(Application app) {
        applicationHolder.set(app);
    }

    public static Application getApplication() {
        return applicationHolder.get();
    }

    public static void setIp(String ip) {
        ipHolder.set(ip);
    }

    public static String getIp() {
        return ipHolder.get();
    }

    public static void setMethod(String method) {
        methodHolder.set(method);
    }

    public static String getMethod() {
        return methodHolder.get();
    }

    public static void setEndpoint(String endpoint) {
        endpointHolder.set(endpoint);
    }

    public static String getEndpoint() {
        return endpointHolder.get();
    }

    public static void clear() {
        applicationHolder.remove();
        ipHolder.remove();
        methodHolder.remove();
        endpointHolder.remove();
    }
}