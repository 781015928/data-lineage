package com.crazypug.datalineage.metadata;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private String currentSchemaName;

    private Map<String, String> envProperties = new HashMap<String, String>();


    public Map<String, String> getEnvProperties() {
        return envProperties;
    }

    public void setEnvProperties(Map<String, String> envProperties) {
        this.envProperties = envProperties;
    }

    public String getCurrentSchemaName() {
        return currentSchemaName;
    }

    public void setCurrentSchemaName(String currentSchemaName) {
        this.currentSchemaName = currentSchemaName;
    }

    public void addEnvProperties(String key, String value) {
        this.envProperties.put(key, value);
    }
}
