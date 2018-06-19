package com.pq.utils;

import io.vertx.core.json.JsonObject;

public class ConfigurationManager {
    public static JsonObject getConfig() {
        return config;
    }

    public static void setConfig(JsonObject config) {
        ConfigurationManager.config = config;
    }

    private static JsonObject config;
}

