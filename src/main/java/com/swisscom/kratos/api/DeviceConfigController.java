package com.swisscom.kratos.api;

import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.service.DeviceConfigService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to query device configuration services
 */
@RestController
@RequestMapping("/api/v1/device")
@AllArgsConstructor
public class DeviceConfigController {

    @Autowired
    private final List<DeviceConfigService> deviceConfigServices;

    private final Map<String, DeviceConfigService> map = new HashMap<>();

    @PostConstruct
    void init() {
        for (DeviceConfigService service : deviceConfigServices) {
            map.put(service.serviceId(), service);
        }
    }

    @GetMapping("/services")
    public Collection<String> listAllServices() {
        return map.keySet();
    }

    @GetMapping("/service/{service}")
    public Collection<String> listDevicesFromService(@PathVariable("service") String service) {
        if (!map.containsKey(service)) {
            throw new RuntimeException("Device service not found");
        }
        return map.get(service).listDevices();
    }

    @GetMapping("/service/{service}/device/{device}")
    public DeviceConfig readDeviceConfig(@PathVariable("service") String service,
            @PathVariable("device") String device) {
        if (!map.containsKey(service)) {
            throw new RuntimeException("Device service not found");
        }
        Optional<DeviceConfig> deviceConfig = map.get(service).fetchConfiguration(device);
        if (deviceConfig.isEmpty()) {
            throw new RuntimeException("Failed to read device config");
        }
        return deviceConfig.get();
    }

}
