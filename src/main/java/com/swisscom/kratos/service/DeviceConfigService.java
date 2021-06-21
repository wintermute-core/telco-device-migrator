package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;

import java.util.Collection;

/**
 * Service to read device configuration
 */
public interface DeviceConfigService {

    Collection<DeviceConfig> readConfiguration();

}
