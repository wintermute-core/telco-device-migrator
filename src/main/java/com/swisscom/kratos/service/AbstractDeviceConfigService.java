package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Common code for configuration service
 */
public abstract class AbstractDeviceConfigService implements DeviceConfigService {

    @Override
    public Map<String, DeviceConfig> readConfiguration(Collection<String> deviceIds) {
        Map<String, DeviceConfig> map = new HashMap<>();
        for(String deviceId : deviceIds) {
            Optional<DeviceConfig> deviceConfig = fetchConfiguration(deviceId);
            deviceConfig.ifPresent(config -> map.put(deviceId, config));
        }
        return map;
    }

}
