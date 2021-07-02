package com.swisscom.kratos.service;

import com.swisscom.kratos.model.Device2Config;
import com.swisscom.kratos.model.DeviceConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Device2ConfigServiceImpl extends AbstractDeviceConfigService {

    private final String configDir;

    public Device2ConfigServiceImpl(@Value("${devices.model2.input}") String configDir) {
        this.configDir = configDir;
    }

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
        if (deviceId.endsWith(".txt")) {
            deviceId = FilenameUtils.removeExtension(deviceId);
        }
        File file = new File(configDir, deviceId + ".txt");
        if (!file.exists()) {
            return Optional.empty();
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            Properties appProps = new Properties();
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
