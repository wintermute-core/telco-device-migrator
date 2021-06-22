package com.swisscom.kratos.service;

import com.swisscom.kratos.model.Device2Config;
import com.swisscom.kratos.model.DeviceConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class Device2ConfigServiceImpl extends AbstractDeviceConfigService {

    @Value("${devices.model2.input}")
    private final String configDir;

    @Override
    public String serviceId() {
        return "model2";
    }

    @Override
    public Collection<String> listDevices() {
        return listFiles(configDir, ".txt");
    }

    @Override
    public Optional<DeviceConfig> fetchConfiguration(String deviceId) {

        File file = new File(configDir, deviceId);
        if (!file.exists()) {
            return Optional.empty();
        }

        Properties appProps = new Properties();
        try (FileInputStream stream = new FileInputStream(file)) {
            appProps.load(stream);
            Map<String, Object> config = new HashMap<>();
            appProps.forEach((key, value) -> config.put(key + "", value));
            Device2Config device2Config = new Device2Config();
            device2Config.setDeviceId(deviceId);
            device2Config.setConfig(config);
            return Optional.of(device2Config);
        } catch (IOException e) {
            log.error("Failed to read config from {}", deviceId, e);
            throw new RuntimeException(e);
        }
    }
}
