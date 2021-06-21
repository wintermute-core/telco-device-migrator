package com.swisscom.kratos.model;

import java.util.Map;

/**
 * Configuration from Device 1
 */
public class Device1Config extends DeviceConfig {

    private Map<String, Object> config;

    public Device1Config(String deviceId, Map<String, Object> config) {
        super(deviceId);
    }

    @Override
    public Map<String, Object> config() {
        return config;
    }
}
