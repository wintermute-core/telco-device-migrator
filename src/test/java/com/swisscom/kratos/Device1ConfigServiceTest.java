package com.swisscom.kratos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.service.Device1ConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class Device1ConfigServiceTest {

    private Device1ConfigServiceImpl device1ConfigService;

    @BeforeEach
    void init() {
        device1ConfigService = new Device1ConfigServiceImpl("input", new ObjectMapper());
    }

    @Test
    void deviceMode1lLoaded() {
        assertEquals("model1", device1ConfigService.serviceId());
    }

    @Test
    void devicesList() {
        Collection<String> listDevices = device1ConfigService.listDevices();
        assertFalse(listDevices.isEmpty());
        assertEquals(2, listDevices.size());
        assertTrue(listDevices.contains("device11"));
        assertTrue(listDevices.contains("device12"));
    }

    @Test
    void deviceConfigLoading() {
        Optional<DeviceConfig> deviceConfig = device1ConfigService.fetchConfiguration("device12.json");
        assertTrue(deviceConfig.isPresent());
        DeviceConfig deviceConfig1 = deviceConfig.get();
        assertEquals("id12", deviceConfig1.getConfig().get("deviceId"));
    }

    @Test
    void deviceConfigLoadingWithoutExtension() {
        Optional<DeviceConfig> deviceConfig = device1ConfigService.fetchConfiguration("device12");
        assertTrue(deviceConfig.isPresent());
        DeviceConfig deviceConfig1 = deviceConfig.get();
        assertEquals("id12", deviceConfig1.getConfig().get("deviceId"));
    }

}
