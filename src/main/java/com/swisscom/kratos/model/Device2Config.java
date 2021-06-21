package com.swisscom.kratos.model;

import java.util.Map;

/**
 * Configuration from Device 2
 */
public class Device2Config extends DeviceConfig {

    private Map<String, Object> config;

    public Device2Config(String deviceId, Map<String, Object> config) {
        super(deviceId);
    }

    @Override
    public Map<String, Object> config() {
        return config;
    }
}
