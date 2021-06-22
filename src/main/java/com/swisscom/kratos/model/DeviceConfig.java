package com.swisscom.kratos.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Device configuration
 */
@Data
public abstract class DeviceConfig {

    private String deviceId;

    private Map<String, Object> config = new HashMap<>();

    public Map<String, Object> config() {
        return config;
    }


}
