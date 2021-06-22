package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class Device2ConfigServiceImpl implements DeviceConfigService{

    @Override
    public String serviceId() {
        return "model2";
    }

    @Override
    public Collection<String> listDevices() {
        return null;
    }

    @Override
    public Optional<DeviceConfig> fetchConfiguration(String deviceId) {
        return Optional.empty();
    }

    @Override
    public Map<String, DeviceConfig> readConfiguration(Collection<String> deviceIds) {
        return null;
    }
}
