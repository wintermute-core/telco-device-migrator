package com.swisscom.kratos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.Device1Config;
import com.swisscom.kratos.model.DeviceConfig;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Device1ConfigServiceImpl extends AbstractDeviceConfigService {

    private final String configDir;

    private final ObjectMapper objectMapper;

    public Device1ConfigServiceImpl(@Value("${devices.model1.input}") String configDir, ObjectMapper objectMapper) {
        this.configDir = configDir;
        this.objectMapper = objectMapper;
    }

    @Override
    public String serviceId() {
        return "model1";
    }

    @Override
    public Collection<String> listDevices() {
        return listFiles(configDir, ".json");
    }

    @Override
    public Optional<DeviceConfig> fetchConfiguration(String deviceId) {
        if (deviceId.endsWith(".json")) {
            deviceId = FilenameUtils.removeExtension(deviceId);
        }

        File file = new File(configDir, deviceId + ".json");
        if (!file.exists()) {
            return Optional.empty();
        }
        try {
            Device1Config device1Config = new Device1Config();
            device1Config.setDeviceId(deviceId);
            HashMap map = objectMapper.readValue(file, HashMap.class);
            device1Config.setConfig(map);
            return Optional.of(device1Config);
        } catch (IOException e) {
            log.error("Failed to read config from {}", deviceId, e);
            throw new RuntimeException(e);
        }
    }


}
