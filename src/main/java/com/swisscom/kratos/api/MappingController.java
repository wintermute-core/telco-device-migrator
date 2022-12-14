package com.swisscom.kratos.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.model.MappingLogic;
import com.swisscom.kratos.model.NetworkService;
import com.swisscom.kratos.service.DeviceConfigService;
import com.swisscom.kratos.service.MappingService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to map devices
 */
@RestController
@RequestMapping("/api/v1/map")
public class MappingController {

    private String currentCode;

    @Autowired
    private MappingService mappingService;

    @Autowired
    @Qualifier("yamlObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    private List<DeviceConfigService> deviceConfigServices;

    private final Map<String, DeviceConfigService> serviceMap = new HashMap<>();

    @PostConstruct
    void init() {
        for (DeviceConfigService service : deviceConfigServices) {
            serviceMap.put(service.serviceId(), service);
        }
        currentCode = mappingService.getMappingLogic().getCode();
    }

    @PostMapping("/logic")
    public void persistLogic(@RequestBody String body) {
        currentCode = body;
        mappingService.setMappingLogic(
                MappingLogic.builder().code(body).build()
        );
    }

    @GetMapping("/logic")
    public String fetchLogic() {
        return currentCode;
    }

    @PostMapping("/run/dry")
    public String dryRun(@RequestParam("serviceId") String serviceId, @RequestParam("deviceId") String deviceId)
            throws JsonProcessingException {

        if (!serviceMap.containsKey(serviceId)) {
            return "Service not found";
        }

        DeviceConfig deviceConfig = null;
        try {
            Map<String, DeviceConfig> deviceConfigMap = serviceMap.get(serviceId).readConfiguration(Set.of(deviceId));
            if (!deviceConfigMap.containsKey(deviceId)) {
                return "Device configuration not found";
            }
            deviceConfig = deviceConfigMap.get(deviceId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read device config", e);
        }
        Collection<NetworkService> networkServices = mappingService
                .dryRun(deviceConfig, MappingLogic.builder().code(currentCode).build());

        return objectMapper.writeValueAsString(networkServices);
    }

    @PostMapping("/run")
    public String startMappingTask() {
        mappingService.setMappingLogic(MappingLogic.builder().code(currentCode).build());
        return mappingService.startMappingTask();
    }

    @DeleteMapping("/run/cancel/{taskId}")
    public void cancelMappingTask(@PathVariable("taskId") String taskId) {
        mappingService.cancelMappingTask(taskId);
    }

    @GetMapping("/run/get/{taskId}")
    public String fetchMappingTaskStatus(@PathVariable("taskId") String taskId) {
        return mappingService.fetchMappingTaskStatus(taskId);
    }

}
