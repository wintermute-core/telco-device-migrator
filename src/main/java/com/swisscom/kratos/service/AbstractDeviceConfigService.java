package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

/**
 * Common code for configuration service
 */
@Slf4j
public abstract class AbstractDeviceConfigService implements DeviceConfigService {

    @Override
    public Map<String, DeviceConfig> readConfiguration(Collection<String> deviceIds) {
        Map<String, DeviceConfig> map = new HashMap<>();
        for (String deviceId : deviceIds) {
            Optional<DeviceConfig> deviceConfig = fetchConfiguration(deviceId);
            deviceConfig.ifPresent(config -> map.put(deviceId, config));
        }
        return map;
    }

    protected Collection<String> listFiles(String dir, String extension) {
        try {
            return Files.list(Path.of(dir))
                    .filter(path -> path.getFileName().toString().endsWith(extension))
                    .map(path -> FilenameUtils.getBaseName(path.getFileName().toString()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to list files {} {}", dir, extension);
            throw new RuntimeException(e);
        }
    }

}
