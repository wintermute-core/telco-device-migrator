package com.swisscom.kratos.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Device configuration
 */
@Data
@AllArgsConstructor
public abstract class DeviceConfig {

    private String deviceId;

    /**
     * Fetch device configuration
     */
    public abstract Map<String, Object> config();


}
