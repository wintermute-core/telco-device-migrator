package com.swisscom.kratos;

import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.service.Device2ConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class Device2ConfigServiceTest {

    private Device2ConfigServiceImpl device2ConfigService;

    @BeforeEach
    void init() {
        device2ConfigService = new Device2ConfigServiceImpl("input");
    }

    @Test
    void deviceMode2lLoaded() {
        assertEquals("model2", device2ConfigService.serviceId());
    }

    @Test
    void devicesList() {
        Collection<String> listDevices = device2ConfigService.listDevices();
        assertFalse(listDevices.isEmpty());
        assertEquals(3, listDevices.size());
        assertTrue(listDevices.contains("device21"));
        assertTrue(listDevices.contains("device22"));
        assertTrue(listDevices.contains("device23"));
    }

    @Test
    void deviceConfigLoading() {
        Optional<DeviceConfig> deviceConfig = device2ConfigService.fetchConfiguration("device22.txt");
        assertTrue(deviceConfig.isPresent());
        DeviceConfig deviceConfig1 = deviceConfig.get();
        assertEquals("uuid22", deviceConfig1.getConfig().get("uuid"));
    }

    @Test
    void deviceConfigWithoutExtensionLoading() {
        Optional<DeviceConfig> deviceConfig = device2ConfigService.fetchConfiguration("device22");
        assertTrue(deviceConfig.isPresent());
        DeviceConfig deviceConfig1 = deviceConfig.get();
        assertEquals("uuid22", deviceConfig1.getConfig().get("uuid"));
    }

}
