package com.LogHub.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private EnvLoader() {
    }

    public static void load() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
