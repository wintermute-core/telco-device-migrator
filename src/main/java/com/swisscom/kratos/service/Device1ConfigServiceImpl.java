package com.swisscom.kratos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.DeviceConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class Device1ConfigServiceImpl extends AbstractDeviceConfigService {

    @Value("${devices.model1.input}")
    private final String configDir;

    private final ObjectMapper objectMapper;

    @Override
    public String serviceId() {
        return "model1";
    }

    @Override
    public Collection<String> listDevices() {
        try {
            return Files.list(Path.of(configDir))
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DeviceConfig> fetchConfiguration(String deviceId) {
        File file = new File(configDir, deviceId);
        if (!file.exists()) {
            return Optional.empty();
        }
        return Optional.empty();
    }


}