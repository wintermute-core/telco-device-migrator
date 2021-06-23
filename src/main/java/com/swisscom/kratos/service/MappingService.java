package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.model.MappingLogic;
import com.swisscom.kratos.model.NetworkService;

import java.util.Collection;

/**
 * Service to map device configuration to NetworkConfiguration.
 */
public interface MappingService {

    MappingLogic getMappingLogic();

    void setMappingLogic(MappingLogic mappingLogic);

    /**
     * Test mapping of one device config to network service
     */
    Collection<NetworkService> dryRun(DeviceConfig config, MappingLogic logic);

    /**
     * Submit for async execution mapping task of all devices to network services.
     */
    String startMappingTask();

    /**
     * Fetch status of submitted task.
     */
    String fetchMappingTaskStatus(String taskId);

    void cancelMappingTask(String taskId);

}
