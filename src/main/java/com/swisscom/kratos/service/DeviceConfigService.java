package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Service to read device configuration
 */
public interface DeviceConfigService {

    /**
     * Return identifier of service
     */
    String serviceId();

    /**
     * List all available devices.
     */
    Collection<String> listDevices();

    /**
     * Fetch configuration of individual device
     */
    Optional<DeviceConfig> fetchConfiguration(String deviceId);

    /**
     * Read devices configuration and provide map with readed data.
     */
    Map<String, DeviceConfig> readConfiguration(Collection<String> deviceIds);

}
