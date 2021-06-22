package com.swisscom.kratos.api;

import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.service.DeviceConfigService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/device")
@AllArgsConstructor
public class DeviceController {

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
        return  map.keySet();
    }

}
